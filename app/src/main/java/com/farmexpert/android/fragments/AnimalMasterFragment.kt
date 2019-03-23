/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/23/19 7:10 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmexpert.android.R
import com.farmexpert.android.adapter.AnimalsAdapter
import com.farmexpert.android.dialogs.AddAnimalDialogFragment
import com.farmexpert.android.model.Animal
import com.farmexpert.android.utils.FarmValidator
import com.farmexpert.android.utils.FirestorePath
import com.farmexpert.android.utils.hidden
import com.farmexpert.android.utils.visible
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.android.synthetic.main.fragment_animal_master.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.longToast
import java.util.*

/**
 * Created by Lucian Iacob on March 22, 2019.
 */
class AnimalMasterFragment : BaseFragment(), AnkoLogger {

    private lateinit var animalsCollections: CollectionReference
    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_animal_master, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animalsCollections = FirebaseFirestore.getInstance()
            .collection(FirestorePath.Collections.FARMS)
            .document(farmId)
            .collection(FirestorePath.Collections.ANIMALS)
        currentUser = FirebaseAuth.getInstance().currentUser!!
        initAnimalList()
    }

    private fun initAnimalList() {
        loadingShow()
        val options = FirestoreRecyclerOptions.Builder<Animal>()
            .setQuery(animalsCollections) { it.toObject(Animal::class.java)!!.apply { id = it.id } }
            .setLifecycleOwner(this)
            .build()

        val adapter =
            object : AnimalsAdapter(options, { animal -> animalClick(animal) }, { animal -> animalLongClick(animal) }) {
                override fun onDataChanged() {
                    loadingHide()
                    val title = if (itemCount != 0) {
                        emptyView.hidden()
                        getString(R.string.headcount_title, itemCount)
                    } else {
                        emptyView.visible()
                        getString(R.string.dashboard_headcount)
                    }
                    (activity as? AppCompatActivity)?.supportActionBar?.title = title
                }
            }

        headcountRecyclerView.layoutManager = LinearLayoutManager(activity)
        headcountRecyclerView.adapter = adapter
    }

    private fun animalClick(animal: Animal) {
        longToast(animal.id!!)
    }

    private fun animalLongClick(animal: Animal) {
        alert(animal.id!!, getString(R.string.delete_animal)) {
            noButton { }
            yesButton {
                loadingShow()
                animalsCollections.document(animal.id!!)
                    .delete()
                    .addOnCompleteListener {
                        loadingHide()
                        if (it.isSuccessful) rootLayout.snackbar(R.string.item_deleted) else alert(it.exception?.message!!)
                    }
            }
        }.show()
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
        val id = extras.getString(AddAnimalDialogFragment.ADD_DIALOG_ID)
        val dateOfBirth = Date(extras.getLong(AddAnimalDialogFragment.ADD_DIALOG_DATE))
        val genre = extras.getString(AddAnimalDialogFragment.ADD_DIALOG_GENRE, "")
        val race = extras.getString(AddAnimalDialogFragment.ADD_DIALOG_RACE, "")
        val fatherId = extras.getString(AddAnimalDialogFragment.ADD_DIALOG_FATHER, "")
        val motherId = extras.getString(AddAnimalDialogFragment.ADD_DIALOG_MOTHER, "")

        if (!FarmValidator.isValidMatricol(id)) {
            alert(R.string.err_adding_animal_message) { yesButton { } }.show()
            return
        }

        loadingShow()
        val animal = Animal(race, Timestamp(dateOfBirth), genre, fatherId, motherId, currentUser.uid)

        animalsCollections.document(id!!)
            .set(animal)
            .addOnSuccessListener {
                rootLayout.snackbar(R.string.item_added)
                loadingHide()
            }
            .addOnFailureListener { exception ->
                loadingHide()
                (exception as? FirebaseFirestoreException)?.let {
                    if (it.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                        alert(R.string.err_adding_animal_constraint) { okButton { } }.show()
                    }
                }
                error { exception }
            }
    }

    override fun onPause() {
        super.onPause()
        addAnimalBtn.setOnClickListener(null)
    }

    companion object {
        const val ADD_ANIMAL_RQ = 678
    }
}