/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.farmexpert.android.R
import com.farmexpert.android.adapter.holder.BreedingViewHolder
import com.farmexpert.android.dialogs.*
import com.farmexpert.android.model.Breeding
import com.farmexpert.android.utils.AppUtils
import com.farmexpert.android.utils.FirestorePath
import com.farmexpert.android.utils.addLoggableFailureListener
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import java.util.*

class BreedingsDetailFragment : BaseDetailFragment<Breeding, BreedingViewHolder>() {

    private val args: BreedingsDetailFragmentArgs by navArgs()

    override val getTitleAndHolderLayout: Pair<Int, Int> =
        Pair(R.string.dashboard_graph_breedings, R.layout.item_breeding)

    override val snapshotParser: SnapshotParser<Breeding> = SnapshotParser {
        it.toObject<Breeding>()!!.apply { id = it.id }
    }

    override fun getAnimalId() = args.animalId

    override fun getAddRecordDialog() = AddBreedingDialogFragment()

    override fun getEditRecordDialog() = EditBreedingDialogFragment()

    override fun createHolder(view: View) =
        BreedingViewHolder(
            itemView = view,
            updateListener = { toUpdate -> showUpdateDialog(toUpdate) },
            deleteListener = { toDelete -> showDeleteDialog(toDelete) }
        )

    override fun getCollectionReference() =
        farmReference.collection(FirestorePath.Collections.BREEDINGS)

    override fun getQuery() =
        getCollectionReference()
            .whereEqualTo(FirestorePath.Breeding.FEMALE, getAnimalId())
            .orderBy(FirestorePath.Breeding.ACTION_DATE, Query.Direction.DESCENDING)

    override fun onNewDataArrived(snapshots: ObservableSnapshotArray<Breeding>) {
        val latestBreeding = snapshots.maxByOrNull { it.actionDate }

        if (latestBreeding?.latestBreeding == false) {
            updateLatestBreedingFlag(breedingToAmend = latestBreeding, flagValue = true)
        }

        snapshots.minusElement(latestBreeding).forEach {
            if (it?.latestBreeding == true) {
                updateLatestBreedingFlag(breedingToAmend = it, flagValue = false)
            }
        }
    }

    override fun constructEntityFromBundle(bundle: Bundle): Any {
        val breedingDate = Date(bundle.getLong(BaseDialogFragment.DIALOG_DATE))
        val male = bundle.getString(BaseAddRecordDialogFragment.ADD_DIALOG_MALE, "")
        val note = bundle.getInt(BaseAddRecordDialogFragment.ADD_DIALOG_NOTE, 5)
        val expectedBirthDate = AppUtils.getExpectedBirthDate(breedingDate, context)
        val details = bundle.getString(BaseAddRecordDialogFragment.ADD_DIALOG_DETAILS)

        return Breeding(
            female = getAnimalId(),
            male = male,
            actionDate = Timestamp(breedingDate),
            note = note,
            birthExpectedAt = Timestamp(expectedBirthDate),
            createdBy = currentUser?.uid,
            comments = details
        )
    }

    private fun updateLatestBreedingFlag(breedingToAmend: Breeding?, flagValue: Boolean) {
        breedingToAmend?.id?.let {
            getCollectionReference().document(it)
                .update(FirestorePath.Breeding.LATEST_BREEDING, flagValue)
                .addLoggableFailureListener()
        }
    }

    override fun getPairsToUpdateFromBundle(args: Bundle): MutableMap<String, Any?> {
        val timestamp = args.getLong(BaseDialogFragment.DIALOG_DATE)
        val newActionDate = Timestamp(Date(timestamp))

        val newNote = args.getInt(
            BaseEditRecordDialogFragment.EDIT_DIALOG_NOTE,
            5
        )

        val newMale = args.getString(BaseEditRecordDialogFragment.EDIT_DIALOG_MALE, "")
        val newComments = args.getString(BaseDialogFragment.DIALOG_DETAILS)

        return mutableMapOf(
            FirestorePath.Breeding.ACTION_DATE to newActionDate,
            FirestorePath.Breeding.NOTE to newNote,
            FirestorePath.Breeding.MALE to newMale,
            FirestorePath.Breeding.COMMENTS to newComments
        )
    }
}