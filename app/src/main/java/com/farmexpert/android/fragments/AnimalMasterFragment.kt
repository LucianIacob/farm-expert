/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/20/19 3:43 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
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
import com.farmexpert.android.utils.FarmValidator
import com.farmexpert.android.utils.FirestorePath
import com.farmexpert.android.utils.hidden
import com.farmexpert.android.utils.visible
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import kotlinx.android.synthetic.main.fragment_animal_master.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.noButton
import org.jetbrains.anko.okButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_headcount, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        with(searchView) {
            setOnQueryTextListener(this@AnimalMasterFragment)
            queryHint = getString(R.string.search_id)
        }

        if (query.isNotEmpty()) {
            searchView.isIconified = false
            searchItem.expandActionView()
            searchView.setQuery(query, true)
            searchView.clearFocus()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

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
            .setQuery(animalsCollections) { it.toObject(Animal::class.java)!!.apply { id = it.id } }
            .build()

        adapter = object : AnimalsAdapter(
            options,
            { animal -> animalClick(animal) },
            { animal -> animalLongClick(animal) }) {
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

    private fun animalLongClick(animal: Animal) {
        alert(
            title = getString(R.string.delete_animal),
            message = getString(R.string.delete_animal_message, animal.id!!)
        ) {
            noButton { }
            yesButton {
                loadingShow()
                DeleteAnimalTransaction(farmReference,
                    { rootLayout.snackbar(R.string.item_deleted) },
                    { exception -> error { exception } },
                    { loadingHide() }).execute(animal.id!!)
            }
        }.show()
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
        if (fragmentManager?.findFragmentByTag(AddAnimalDialogFragment.TAG) == null) {
            addDialog.show(fragmentManager!!, AddAnimalDialogFragment.TAG)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data?.extras == null) return
        when (requestCode) {
            ADD_ANIMAL_RQ -> insertAnimal(data.extras!!)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun insertAnimal(extras: Bundle) {
        val id = extras.getString(BaseAddRecordDialogFragment.ADD_DIALOG_ANIMAL, "")
        val dateOfBirth = Date(extras.getLong(BaseAddRecordDialogFragment.ADD_DIALOG_DATE))
        val gender = extras.getString(BaseAddRecordDialogFragment.ADD_DIALOG_GENDER, "")
        val race = extras.getString(BaseAddRecordDialogFragment.ADD_DIALOG_RACE, "")
        val fatherId = extras.getString(BaseAddRecordDialogFragment.ADD_DIALOG_FATHER, "")
        val motherId = extras.getString(BaseAddRecordDialogFragment.ADD_DIALOG_MOTHER, "")

        if (!FarmValidator.isValidAnimalId(id)) {
            alert(R.string.err_adding_animal_message) { yesButton { } }.show()
            return
        }

        loadingShow()
        val animal = Animal(
            race = race,
            dateOfBirth = Timestamp(dateOfBirth),
            gender = gender,
            fatherId = fatherId,
            motherId = motherId,
            createdBy = currentUser?.uid
        )

        animalsCollections.document(id!!)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    alert(R.string.err_adding_animal_constraint) { okButton { } }.show()
                } else {
                    animalsCollections.document(id)
                        .set(animal)
                        .addOnSuccessListener { rootLayout.snackbar(R.string.item_added) }
                        .addOnFailureListener {
                            alert(R.string.err_adding_animal) { okButton { } }.show()
                            error { it }
                        }
                }
            }
            .addOnFailureListener { alert(R.string.err_adding_animal) { okButton { } }.show() }
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