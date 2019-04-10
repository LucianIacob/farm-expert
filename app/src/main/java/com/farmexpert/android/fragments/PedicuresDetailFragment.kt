/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/10/19 9:16 PM.
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
import com.farmexpert.android.model.AnimalAction
import com.farmexpert.android.utils.FirestorePath
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import java.util.*

class PedicuresDetailFragment : BaseDetailFragment<AnimalAction, PedicureViewHolder>() {

    override val snapshotParser: SnapshotParser<AnimalAction> = SnapshotParser {
        it.toObject(AnimalAction::class.java)!!.apply { id = it.id }
    }

    override fun getTitleAndHolderLayout(): Pair<String, Int> =
        Pair(getString(R.string.dashboard_graph_pedicures), R.layout.item_animal_action)

    private val args: PedicuresDetailFragmentArgs by navArgs()

    override fun getAnimalId() = args.animalId

    override fun getAddRecordDialog() = AddPedicureDialogFragment()

    override fun getCollectionReference() =
        farmReference.collection(FirestorePath.Collections.PEDICURES)

    override fun createHolder(view: View): PedicureViewHolder {
        return PedicureViewHolder(view,
            { pedicureToUpdate -> showUpdateDialog(pedicureToUpdate) },
            { pedicureToDelete -> showDeleteDialog(pedicureToDelete) })
    }

    private fun showDeleteDialog(pedicureToDelete: AnimalAction) {

    }

    private fun showUpdateDialog(pedicureToUpdate: AnimalAction) {

    }

    override fun getQuery(): Query {
        return getCollectionReference()
            .whereEqualTo(FirestorePath.AnimalAction.ANIMAL_ID, getAnimalId())
    }

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

}