/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 10/23/19 12:26 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmexpert.android.NavGraphDirections
import com.farmexpert.android.R
import com.farmexpert.android.adapter.AnimalsAdapter
import com.farmexpert.android.dialogs.AddAnimalDialogFragment
import com.farmexpert.android.dialogs.BaseAddRecordDialogFragment
import com.farmexpert.android.model.Animal
import com.farmexpert.android.transactions.DeleteAnimalTransaction
import com.farmexpert.android.utils.*
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_animal_master.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.design.snackbar
import java.util.*

/**
 * Created by Lucian Iacob on March 22, 2019.
 */
class AnimalMasterFragment : BaseFragment(), AnkoLogger, SearchView.OnQueryTextListener {

    private lateinit var animalsCollections: CollectionReference

    private lateinit var adapter: AnimalsAdapter

    private var query: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_headcount, menu)
//        val searchItem = menu.findItem(R.id.action_search)
//        val searchView = searchItem?.actionView as SearchView
//        with(searchView) {
//            setOnQueryTextListener(this@AnimalMasterFragment)
//            queryHint = getString(R.string.search_id)
//        }
//
//        if (query.isNotEmpty()) {
//            searchView.isIconified = false
//            searchItem.expandActionView()
//            searchView.setQuery(query, true)
//            searchView.clearFocus()
//        }
//        super.onCreateOptionsMenu(menu, inflater)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animal_master, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animalsCollections = farmReference.collection(FirestorePath.Collections.ANIMALS)
        initAnimalList()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.getString(KEY_QUERY)?.let { query = it }
    }

    override fun onViewReady() {
        super.onViewReady()
        adapter.readyForListening()
    }

    private fun initAnimalList() {
        loadingShow()
        val options = FirestoreRecyclerOptions.Builder<Animal>()
            .setQuery(animalsCollections) { it.toObject<Animal>()!!.apply { id = it.id } }
            .build()

        adapter = object : AnimalsAdapter(
            options,
            { animal -> animalClick(animal) },
            { animalId -> animalLongClick(animalId) }) {
            override fun onDataChanged() {
                loadingHide()
                (activity as? AppCompatActivity)?.supportActionBar?.title = if (itemCount != 0) {
                    emptyView.hidden()
                    getString(R.string.headcount_title, itemCount)
                } else {
                    emptyView.visible()
                    getString(R.string.dashboard_headcount)
                }
            }
        }

        headcountRecyclerView.layoutManager = LinearLayoutManager(activity)
        headcountRecyclerView.adapter = adapter
    }

    private fun animalClick(animal: Animal) {
        animal.id?.let {
            val direction = NavGraphDirections.actionGlobalAnimalDetailFragment(
                animalId = it,
                shouldGetFromCache = true
            )
            NavHostFragment.findNavController(this).navigate(direction)
        }
    }

    private fun animalLongClick(animalId: String) {
        context?.let { context ->
            AlertDialog.Builder(context, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                .setTitle(R.string.delete_animal)
                .setMessage(getString(R.string.delete_animal_message, animalId))
                .setPositiveButton(R.string.delete) { _, _ ->
                    loadingShow()
                    DeleteAnimalTransaction(farmReference,
                        { rootLayout?.snackbar(R.string.item_deleted) },
                        { exception -> error { exception } },
                        { loadingHide() })
                        .execute(animalId)
                }
                .setNegativeButton(R.string.fui_cancel) { _, _ -> }
                .setCancelable(false)
                .create()
                .run {
                    setOnShowListener {
                        getButton(DialogInterface.BUTTON_NEGATIVE).applyFarmexpertStyle(context)
                        getButton(DialogInterface.BUTTON_POSITIVE).applyFarmexpertStyle(
                            context,
                            redButton = true
                        )
                    }
                    show()
                }
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onResume() {
        super.onResume()
        addAnimalBtn.setOnClickListener { addAnimalClicked() }
    }

    private fun addAnimalClicked() {
        val addDialog = AddAnimalDialogFragment()
        addDialog.setTargetFragment(this, ADD_ANIMAL_RQ)
        fragmentManager?.run {
            if (findFragmentByTag(AddAnimalDialogFragment.TAG) == null) {
                addDialog.show(this, AddAnimalDialogFragment.TAG)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data?.extras == null) return
        when (requestCode) {
            ADD_ANIMAL_RQ -> data.extras?.let { insertAnimal(it) }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun insertAnimal(extras: Bundle) {
        val id = extras.getString(BaseAddRecordDialogFragment.ADD_DIALOG_ANIMAL, "")
        val dateOfBirth = Date(extras.getLong(BaseAddRecordDialogFragment.ADD_DIALOG_DATE))
        val gender = extras.getInt(BaseAddRecordDialogFragment.ADD_DIALOG_GENDER, 0)
        val race = extras.getString(BaseAddRecordDialogFragment.ADD_DIALOG_RACE, "")
        val fatherId = extras.getString(BaseAddRecordDialogFragment.ADD_DIALOG_FATHER, "")
        val motherId = extras.getString(BaseAddRecordDialogFragment.ADD_DIALOG_MOTHER, "")

        if (!FarmValidator.isValidAnimalId(id)) {
            alert(message = R.string.err_adding_animal_message)
            return
        }

        val animal = Animal(
            race = race,
            dateOfBirth = Timestamp(dateOfBirth),
            gender = gender,
            fatherId = fatherId,
            motherId = motherId,
            createdBy = currentUser?.uid
        )

        loadingShow()
        animalsCollections.document(id)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    alert(message = R.string.err_adding_animal_constraint)
                } else {
                    animalsCollections.document(id)
                        .set(animal)
                        .addOnSuccessListener { rootLayout?.snackbar(R.string.item_added) }
                        .addOnFailureListener {
                            alert(message = R.string.err_adding_animal)
                            error { it }
                        }
                }
            }
            .addOnFailureListener { alert(R.string.err_adding_animal) }
            .addOnCompleteListener { loadingHide() }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { this.query = it }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        query?.let { this.query = it }
        return true
    }

    override fun onPause() {
        super.onPause()
        addAnimalBtn.setOnClickListener(null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_QUERY, query)
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    companion object {
        const val ADD_ANIMAL_RQ = 678
        const val KEY_QUERY = "com.farmexpert.android.Query"
    }
}