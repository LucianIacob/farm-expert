/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 10/23/19 12:26 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.farmexpert.android.R
import com.farmexpert.android.adapter.holder.BirthViewHolder
import com.farmexpert.android.dialogs.*
import com.farmexpert.android.model.Animal
import com.farmexpert.android.model.Birth
import com.farmexpert.android.utils.FirestorePath
import com.farmexpert.android.utils.addLoggableFailureListener
import com.farmexpert.android.utils.alert
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import java.util.*

class BirthsDetailFragment : BaseDetailFragment<Birth, BirthViewHolder>() {

    private val args: BirthsDetailFragmentArgs by navArgs()

    private val animalsCollections
        by lazy { farmReference.collection(FirestorePath.Collections.ANIMALS) }

    override fun getAnimalId() = args.animalId

    override fun getTitleAndHolderLayout(): Pair<String, Int> {
        return Pair(getString(R.string.dashboard_graph_births), R.layout.item_birth)
    }

    override fun getAddRecordDialog() = AddBirthDialogFragment()

    override fun getEditRecordDialog() = EditBirthDialogFragment()

    override val snapshotParser: SnapshotParser<Birth> = SnapshotParser {
        it.toObject<Birth>()!!.apply { id = it.id }
    }

    override fun onNewDataArrived(snapshots: ObservableSnapshotArray<Birth>) {
        val latestBirth = snapshots.maxByOrNull { it.dateOfBirth }

        if (latestBirth?.latestBirth == false) {
            updateLatestBirthFlag(birthToAmend = latestBirth, flagValue = true)
        }

        snapshots.minusElement(latestBirth).forEach { birth ->
            if (birth?.latestBirth == true) {
                updateLatestBirthFlag(birthToAmend = birth, flagValue = false)
            }
        }
    }

    private fun updateLatestBirthFlag(birthToAmend: Birth?, flagValue: Boolean) {
        birthToAmend?.id?.let { birthId ->
            getCollectionReference().document(birthId)
                .update(FirestorePath.Birth.LATEST_BIRTH, flagValue)
                .addLoggableFailureListener()
        }
    }

    override fun constructEntityFromBundle(bundle: Bundle): Birth {
        val calfId = bundle.getString(BaseAddRecordDialogFragment.ADD_DIALOG_CALF, "")
        val dateOfBirth = Date(bundle.getLong(BaseDialogFragment.DIALOG_DATE))
        val note = bundle.getInt(BaseAddRecordDialogFragment.ADD_DIALOG_NOTE, 5)
        val details = bundle.getString(BaseAddRecordDialogFragment.ADD_DIALOG_DETAILS)

        return Birth(
            calfId = calfId,
            dateOfBirth = Timestamp(dateOfBirth),
            motherId = getAnimalId(),
            note = note,
            createdBy = currentUser?.uid,
            comments = details
        )
    }

    override fun getPairsToUpdateFromBundle(args: Bundle): MutableMap<String, Any?> {
        val timestamp = args.getLong(BaseDialogFragment.DIALOG_DATE)
        val newActionDate = Timestamp(Date(timestamp))

        val newNote = args.getInt(
            BaseEditRecordDialogFragment.EDIT_DIALOG_NOTE,
            4
        )

        val newComments = args.getString(BaseDialogFragment.DIALOG_DETAILS)

        return mutableMapOf(
            FirestorePath.Birth.DATE_OF_BIRTH to newActionDate,
            FirestorePath.Birth.NOTE to newNote,
            FirestorePath.Birth.COMMENTS to newComments
        )
    }

    override fun validateEntity(entity: Any, listener: (Boolean) -> Unit) {
        animalsCollections.document((entity as Birth).calfId)
            .get()
            .addLoggableFailureListener {
                alert(R.string.err_validating_calf)
                listener(false)
                loadingHide()
            }
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    alert(R.string.err_adding_animal_constraint)
                    loadingHide()
                }
                listener(!snapshot.exists())
            }
    }

    override fun getCollectionReference(): CollectionReference {
        return farmReference.collection(FirestorePath.Collections.BIRTHS)
    }

    override fun getQuery() =
        getCollectionReference()
            .whereEqualTo(FirestorePath.Birth.MOTHER_ID, getAnimalId())
            .orderBy(FirestorePath.Birth.DATE_OF_BIRTH, Query.Direction.DESCENDING)

    override fun addDependentData(entity: Any) {
        val digits = resources.getInteger(R.integer.animal_id_digits_to_show)

        (entity as? Birth)?.let {
            val animal = Animal(
                dateOfBirth = it.dateOfBirth,
                gender = 0,
                motherId = getAnimalId(),
                createdBy = currentUser?.uid,
                lastDigits = it.calfId.takeLast(digits)
            )

            animalsCollections.document(it.calfId)
                .set(animal)
                .addLoggableFailureListener { alert(R.string.err_adding_animal_from_birth) }
        }
    }

    override fun createHolder(view: View) =
        BirthViewHolder(
            itemView = view,
            updateListener = { birthToUpdate -> showUpdateDialog(birthToUpdate) },
            deleteListener = { birthToDelete -> showDeleteDialog(birthToDelete) }
        )
}
