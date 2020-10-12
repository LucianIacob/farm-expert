/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/19/19 9:36 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.farmexpert.android.adapter.holder.AnimalActionHolder
import com.farmexpert.android.dialogs.AddAnimalActionDialogFragment
import com.farmexpert.android.dialogs.AddAnimalActionDialogFragment.Companion.ADD_DIALOG_TITLE
import com.farmexpert.android.dialogs.BaseAddRecordDialogFragment
import com.farmexpert.android.dialogs.BaseDialogFragment
import com.farmexpert.android.dialogs.EditAnimalActionDialogFragment
import com.farmexpert.android.dialogs.EditAnimalActionDialogFragment.Companion.EDIT_DIALOG_TITLE
import com.farmexpert.android.model.AnimalAction
import com.farmexpert.android.utils.FirestorePath
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import java.util.*

abstract class BaseAnimalActionDetailFragment :
    BaseDetailFragment<AnimalAction, AnimalActionHolder>() {

    override val snapshotParser: SnapshotParser<AnimalAction> = SnapshotParser {
        it.toObject<AnimalAction>()!!.apply { id = it.id }
    }

    override fun createHolder(view: View) =
        AnimalActionHolder(
            itemView = view,
            updateListener = { actionToUpdate -> showUpdateDialog(actionToUpdate) },
            deleteListener = { actionToDelete -> showDeleteDialog(actionToDelete) }
        )

    override fun constructEntityFromBundle(bundle: Bundle): Any {
        val details = bundle.getString(BaseAddRecordDialogFragment.ADD_DIALOG_DETAILS, "")
        val actionDate = Date(bundle.getLong(BaseDialogFragment.DIALOG_DATE))

        return AnimalAction(
            animalId = getAnimalId(),
            actionDate = Timestamp(actionDate),
            details = details,
            createdBy = currentUser?.uid
        )
    }

    override fun getPairsToUpdateFromBundle(args: Bundle): MutableMap<String, Any?> {
        val timestamp = args.getLong(BaseDialogFragment.DIALOG_DATE)
        val newActionDate = Timestamp(Date(timestamp))

        val newDetails = args.getString(BaseDialogFragment.DIALOG_DETAILS, "")

        return mutableMapOf(
            FirestorePath.AnimalAction.ACTION_DATE to newActionDate,
            FirestorePath.AnimalAction.DETAILS to newDetails
        )
    }

    override fun getAddRecordDialog() = AddAnimalActionDialogFragment().apply {
        arguments = bundleOf(ADD_DIALOG_TITLE to getAddDialogTitle())
    }

    override fun getEditRecordDialog() = EditAnimalActionDialogFragment().apply {
        arguments = bundleOf(EDIT_DIALOG_TITLE to getEditDialogTitle())
    }

    override fun getQuery(): Query {
        return getCollectionReference()
            .whereEqualTo(FirestorePath.AnimalAction.ANIMAL_ID, getAnimalId())
            .orderBy(FirestorePath.AnimalAction.ACTION_DATE, Query.Direction.DESCENDING)
    }

    abstract fun getAddDialogTitle(): Int

    abstract fun getEditDialogTitle(): Int
}