/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 7/13/19 11:19 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.farmexpert.android.R
import com.farmexpert.android.adapter.holder.BreedingViewHolder
import com.farmexpert.android.dialogs.AddBreedingDialogFragment
import com.farmexpert.android.dialogs.BaseAddRecordDialogFragment
import com.farmexpert.android.dialogs.BaseEditRecordDialogFragment
import com.farmexpert.android.dialogs.EditBreedingDialogFragment
import com.farmexpert.android.model.Breeding
import com.farmexpert.android.utils.AppUtils
import com.farmexpert.android.utils.FirestorePath
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import org.jetbrains.anko.error
import java.util.*

class BreedingsDetailFragment : BaseDetailFragment<Breeding, BreedingViewHolder>() {

    private val args: BreedingsDetailFragmentArgs by navArgs()
    private var latestBreeding: Breeding? = null

    override val snapshotParser: SnapshotParser<Breeding> = SnapshotParser {
        it.toObject(Breeding::class.java)!!.apply { id = it.id }
    }

    override fun getAnimalId() = args.animalId

    override fun getTitleAndHolderLayout(): Pair<String, Int> =
        Pair(getString(R.string.dashboard_graph_breedings), R.layout.item_breeding)

    override fun getAddRecordDialog() = AddBreedingDialogFragment()

    override fun getEditRecordDialog() = EditBreedingDialogFragment()

    override fun createHolder(view: View): BreedingViewHolder {
        return BreedingViewHolder(view,
            { breedingToUpdate -> showUpdateDialog(breedingToUpdate) },
            { breedingToDelete -> showDeleteDialog(breedingToDelete) })
    }

    override fun getCollectionReference(): CollectionReference {
        return farmReference.collection(FirestorePath.Collections.BREEDINGS)
    }

    override fun getQuery(): Query {
        return getCollectionReference()
            .whereEqualTo(FirestorePath.Breeding.FEMALE, getAnimalId())
            .orderBy(FirestorePath.Breeding.ACTION_DATE, Query.Direction.DESCENDING)
    }

    override fun onNewDataArrived(snapshots: ObservableSnapshotArray<Breeding>) {
        latestBreeding = snapshots.maxBy { it.actionDate }
    }

    override fun constructEntityFromBundle(bundle: Bundle): Any {
        val breedingDate = Date(bundle.getLong(BaseAddRecordDialogFragment.ADD_DIALOG_DATE))
        val male = bundle.getString(BaseAddRecordDialogFragment.ADD_DIALOG_MALE, "")
        val note = bundle.getString(BaseAddRecordDialogFragment.ADD_DIALOG_NOTE, "")
        val expectedBirthDate = AppUtils.getExpectedBirthDate(breedingDate)

        val breedingToAdd = Breeding(
            female = getAnimalId(),
            male = male,
            actionDate = Timestamp(breedingDate),
            note = note,
            birthExpectedAt = Timestamp(expectedBirthDate),
            createdBy = currentUser?.uid,
            latestBreeding = true
        )

        latestBreeding?.let {
            if (breedingToAdd.actionDate.compareTo(it.actionDate) == 1) {
                removeLatestBreedingFlag(breedingToAmend = it)
                latestBreeding = breedingToAdd
            } else {
                breedingToAdd.latestBreeding = false
            }
        }

        return breedingToAdd
    }

    private fun removeLatestBreedingFlag(breedingToAmend: Breeding) {
        breedingToAmend.id?.let {
            getCollectionReference().document(it)
                .update(FirestorePath.Breeding.LATEST_BREEDING, false)
                .addOnFailureListener { error { it } }
        }
    }

    override fun getPairsToUpdateFromBundle(args: Bundle): MutableMap<String, Any> {
        val timestamp = args.getLong(BaseEditRecordDialogFragment.EDIT_DIALOG_DATE)
        val newActionDate = Timestamp(Date(timestamp))

        val newNote = args.getString(
            BaseEditRecordDialogFragment.EDIT_DIALOG_NOTE,
            getString(R.string.default_breeding_note)
        )

        val newMale = args.getString(BaseEditRecordDialogFragment.EDIT_DIALOG_MALE, "")

        return mutableMapOf(
            FirestorePath.Breeding.ACTION_DATE to newActionDate,
            FirestorePath.Breeding.NOTE to newNote,
            FirestorePath.Breeding.MALE to newMale
        )
    }
}