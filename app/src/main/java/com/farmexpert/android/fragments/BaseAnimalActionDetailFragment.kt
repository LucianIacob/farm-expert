/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/10/19 9:18 AM.
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
import com.farmexpert.android.model.AnimalAction
import com.farmexpert.android.utils.FirestorePath
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import java.util.*

abstract class BaseAnimalActionDetailFragment :
    BaseDetailFragment<AnimalAction, AnimalActionHolder>() {

    override val snapshotParser: SnapshotParser<AnimalAction> = SnapshotParser {
        it.toObject(AnimalAction::class.java)!!.apply { id = it.id }
    }

    override fun createHolder(view: View): AnimalActionHolder = AnimalActionHolder(view)

    override fun constructEntityFromBundle(bundle: Bundle): Any {
        val details = bundle.getString(BaseAddRecordDialogFragment.ADD_DIALOG_DETAILS, "")
        val actionDate = Date(bundle.getLong(BaseAddRecordDialogFragment.ADD_DIALOG_DATE))

        return AnimalAction(
            animalId = getAnimalId(),
            actionDate = Timestamp(actionDate),
            details = details,
            createdBy = currentUser?.uid
        )
    }

    override fun getAddRecordDialog() = AddAnimalActionDialogFragment().apply {
        arguments = bundleOf(ADD_DIALOG_TITLE to getAddDialogTitle())
    }

    override fun getQuery(): Query {
        return getCollectionReference()
            .whereEqualTo(FirestorePath.AnimalAction.ANIMAL_ID, getAnimalId())
    }

    abstract fun getAddDialogTitle(): Int
}