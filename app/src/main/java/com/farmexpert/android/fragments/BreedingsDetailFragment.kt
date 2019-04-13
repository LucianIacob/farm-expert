/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/13/19 10:04 PM.
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
import com.farmexpert.android.dialogs.EditBreedingDialogFragment
import com.farmexpert.android.model.Breeding
import com.farmexpert.android.utils.AppUtils
import com.farmexpert.android.utils.FirestorePath
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import java.util.*

class BreedingsDetailFragment : BaseDetailFragment<Breeding, BreedingViewHolder>() {

    private val args: BreedingsDetailFragmentArgs by navArgs()

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
    }

    override fun constructEntityFromBundle(bundle: Bundle): Any {
        val date = Date(bundle.getLong(BaseAddRecordDialogFragment.ADD_DIALOG_DATE))
        val male = bundle.getString(BaseAddRecordDialogFragment.ADD_DIALOG_MALE, "")
        val note = bundle.getString(BaseAddRecordDialogFragment.ADD_DIALOG_NOTE, "")
        val expectedBirth = AppUtils.getExpectedBirth(date)

        return Breeding(
            female = getAnimalId(),
            male = male,
            actionDate = Timestamp(date),
            note = note,
            birthExpectedAt = Timestamp(expectedBirth),
            createdBy = currentUser?.uid
        )
    }
}