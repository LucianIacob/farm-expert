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
import com.farmexpert.android.adapter.holder.PedicureViewHolder
import com.farmexpert.android.dialogs.AddPedicureDialogFragment
import com.farmexpert.android.dialogs.BaseAddRecordDialogFragment
import com.farmexpert.android.dialogs.BaseDialogFragment
import com.farmexpert.android.dialogs.EditPedicureDialogFragment
import com.farmexpert.android.model.AnimalAction
import com.farmexpert.android.utils.FirestorePath
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import java.util.*

class PedicuresDetailFragment : BaseDetailFragment<AnimalAction, PedicureViewHolder>() {

    override val snapshotParser: SnapshotParser<AnimalAction> = SnapshotParser {
        it.toObject<AnimalAction>()!!.apply { id = it.id }
    }

    override val getTitleAndHolderLayout: Pair<Int, Int> =
        Pair(R.string.dashboard_graph_pedicures, R.layout.item_animal_action)

    private val args: PedicuresDetailFragmentArgs by navArgs()

    override fun getAnimalId() = args.animalId

    override fun getAddRecordDialog() = AddPedicureDialogFragment()

    override fun getEditRecordDialog() = EditPedicureDialogFragment()

    override fun getCollectionReference() =
        farmReference.collection(FirestorePath.Collections.PEDICURES)

    override fun createHolder(view: View) =
        PedicureViewHolder(
            itemView = view,
            updateListener = { pedicureToUpdate -> showUpdateDialog(pedicureToUpdate) },
            deleteListener = { pedicureToDelete -> showDeleteDialog(pedicureToDelete) }
        )

    override fun getQuery() =
        getCollectionReference()
            .whereEqualTo(FirestorePath.AnimalAction.ANIMAL_ID, getAnimalId())
            .orderBy(FirestorePath.AnimalAction.ACTION_DATE, Query.Direction.DESCENDING)

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

        val newDetails = args.getString(
            BaseDialogFragment.DIALOG_DETAILS,
            "00000000"
        )

        return mutableMapOf(
            FirestorePath.AnimalAction.ACTION_DATE to newActionDate,
            FirestorePath.AnimalAction.DETAILS to newDetails
        )
    }

}