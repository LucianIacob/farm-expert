/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/9/19 9:25 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.farmexpert.android.R
import com.farmexpert.android.adapter.holder.AnimalActionHolder
import com.farmexpert.android.dialogs.AddAnimalActionDialogFragment
import com.farmexpert.android.dialogs.BaseAddRecordDialogFragment
import com.farmexpert.android.model.AnimalAction
import com.farmexpert.android.utils.FirestorePath
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import java.util.*

class VitaminizationsDetailFragment : BaseDetailFragment<AnimalAction, AnimalActionHolder>() {

    override val snapshotParser: SnapshotParser<AnimalAction> = SnapshotParser {
        it.toObject(AnimalAction::class.java)!!.apply { id = it.id }
    }

    override fun createHolder(view: View) = AnimalActionHolder(view)

    override fun getTitleAndHolderLayout(): Pair<String, Int> =
        Pair(getString(R.string.dashboard_graph_vitaminisations), R.layout.item_animal_action)

    private val args: VitaminizationsDetailFragmentArgs by navArgs()

    override fun getAnimalId() = args.animalId

    override fun getAddRecordDialog() = AddAnimalActionDialogFragment().apply {
        arguments =
            bundleOf(AddAnimalActionDialogFragment.ADD_DIALOG_TITLE to R.string.add_vitaminization_title)
    }

    override fun getQuery(): Query {
        return getCollectionReference()
            .whereEqualTo(FirestorePath.AnimalAction.ANIMAL_ID, getAnimalId())
    }

    override fun getCollectionReference() =
        farmReference.collection(FirestorePath.Collections.VITAMINIZATIONS)

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