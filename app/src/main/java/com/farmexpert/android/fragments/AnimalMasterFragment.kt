/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmexpert.android.NavGraphDirections
import com.farmexpert.android.R
import com.farmexpert.android.adapter.AnimalsAdapter
import com.farmexpert.android.dialogs.AddAnimalDialogFragment
import com.farmexpert.android.dialogs.BaseAddRecordDialogFragment
import com.farmexpert.android.dialogs.BaseDialogFragment
import com.farmexpert.android.model.Animal
import com.farmexpert.android.transactions.DeleteAnimalTransaction
import com.farmexpert.android.utils.*
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_animal_master.*
import kotlinx.coroutines.delay
import java.util.*

/**
 * Created by Lucian Iacob on March 22, 2019.
 */
class AnimalMasterFragment : BaseFragment(R.layout.fragment_animal_master),
    SearchView.OnQueryTextListener {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var animalsCollections: CollectionReference

    private lateinit var adapter: AnimalsAdapter

    private var layoutState: Parcelable? = null
    private var query: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animalsCollections = farmReference.collection(FirestorePath.Collections.ANIMALS)

        PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_LAYOUT_STATE, null)?.let {
                layoutState = Gson().fromJson(it, LinearLayoutManager.SavedState::class.java)
                PreferenceManager.getDefaultSharedPreferences(context)
                    .edit { remove(KEY_LAYOUT_STATE) }
            }

        initAnimalList()
        lifecycleScope.launchWhenStarted {
            loadingShow()
            delay(250)
            adapter.startListening()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.run {
            getString(KEY_QUERY)?.let { query = it }
            layoutState = getParcelable(KEY_LAYOUT_STATE)
        }
    }

    private fun initAnimalList() {
        val options = FirestoreRecyclerOptions.Builder<Animal>()
            .setQuery(getAnimalsQuery()) { it.toObject<Animal>()!!.apply { id = it.id } }
            .build()

        adapter = object : AnimalsAdapter(
            options = options,
            clickListener = { animal -> animalClick(animal) },
            longClickListener = { animalId -> animalLongClick(animalId) }
        ) {
            override fun onDataChanged() {
                loadingHide()
                emptyView.isVisible = itemCount == 0
                (activity as? AppCompatActivity)?.supportActionBar?.title = if (itemCount != 0) {
                    layoutState?.let { layoutManager.onRestoreInstanceState(it) }
                    getString(R.string.headcount_title, itemCount)
                } else {
                    getString(R.string.dashboard_headcount)
                }
            }
        }

        layoutManager = headcountRecyclerView.layoutManager as LinearLayoutManager
        headcountRecyclerView.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    private fun getAnimalsQuery(): Query =
        if (resources.getBoolean(R.bool.sort_by_last_digits))
            animalsCollections.orderBy(FirestorePath.Animal.LAST_DIGITS)
        else animalsCollections

    private fun animalClick(animal: Animal) {
        animal.id?.let {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit {
                    val state = Gson().toJson(
                        layoutManager.onSaveInstanceState() as? LinearLayoutManager.SavedState
                    )
                    putString(KEY_LAYOUT_STATE, state)
                }

            val direction = NavGraphDirections.actionGlobalAnimalDetailFragment(
                animalId = it,
                shouldGetFromCache = true
            )
            NavHostFragment.findNavController(this).navigate(direction)
        }
    }

    private fun animalLongClick(animalId: String) {
        context?.let { context ->
            MaterialAlertDialogBuilder(context)
                .setTitle(R.string.delete_animal)
                .setMessage(getString(R.string.delete_animal_message, animalId))
                .setPositiveButton(R.string.delete) { _, _ ->
                    loadingShow()
                    DeleteAnimalTransaction(
                        farmReference = farmReference,
                        successListener = { rootLayout?.snackbar(R.string.item_deleted) },
                        failureListener = { exception -> error { exception } },
                        complete = { loadingHide() }
                    ).execute(animalId)
                }
                .setNegativeButton(R.string.fui_cancel) { _, _ -> }
                .create()
                .run {
                    setOnShowListener {
                        getButton(DialogInterface.BUTTON_POSITIVE).applyFarmexpertStyle(
                            context,
                            redButton = true
                        )
                    }
                    show()
                }
        }
    }

    override fun onResume() {
        super.onResume()
        addAnimalBtn.setOnClickListener { addAnimalClicked() }
    }

    private fun addAnimalClicked() {
        val addDialog = AddAnimalDialogFragment()
        addDialog.setTargetFragment(this, ADD_ANIMAL_RQ)
        parentFragmentManager.run {
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
        val dateOfBirth = Date(extras.getLong(BaseDialogFragment.DIALOG_DATE))
        val gender = extras.getInt(BaseAddRecordDialogFragment.ADD_DIALOG_GENDER, 0)
        val race = extras.getString(BaseAddRecordDialogFragment.ADD_DIALOG_RACE, "")
        val fatherId = extras.getString(BaseAddRecordDialogFragment.ADD_DIALOG_FATHER, "")
        val motherId = extras.getString(BaseAddRecordDialogFragment.ADD_DIALOG_MOTHER, "")

        if (!isValidAnimalId(id)) {
            alert(message = R.string.err_adding_animal_message)
            return
        }

        val digits = resources.getInteger(R.integer.animal_id_digits_to_show)

        val animal = Animal(
            race = race,
            dateOfBirth = Timestamp(dateOfBirth),
            gender = gender,
            fatherId = fatherId,
            motherId = motherId,
            createdBy = currentUser?.uid,
            lastDigits = id.takeLast(digits)
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
                        .addLoggableFailureListener { alert(message = R.string.err_adding_animal) }
                        .addOnCompleteListener {
                            parentFragmentManager.findFragmentByTag(AddAnimalDialogFragment.TAG)
                                ?.let { parentFragmentManager.commit { remove(it) } }
                        }
                }
            }
            .addLoggableFailureListener { alert(R.string.err_adding_animal) }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_QUERY, query)
        if (::layoutManager.isInitialized) {
            outState.putParcelable(KEY_LAYOUT_STATE, layoutManager.onSaveInstanceState())
        }
    }

    companion object {
        const val ADD_ANIMAL_RQ = 678
        const val KEY_QUERY = "com.farmexpert.android.Query"
        const val KEY_LAYOUT_STATE = "com.farmexpert.android.LayoutState"
    }
}