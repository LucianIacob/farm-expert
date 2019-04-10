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
import com.farmexpert.android.adapter.holder.BirthViewHolder
import com.farmexpert.android.dialogs.AddBirthDialogFragment
import com.farmexpert.android.dialogs.BaseAddRecordDialogFragment
import com.farmexpert.android.model.Animal
import com.farmexpert.android.model.Birth
import com.farmexpert.android.utils.FirestorePath
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import org.jetbrains.anko.okButton
import org.jetbrains.anko.support.v4.alert
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

    override val snapshotParser: SnapshotParser<Birth> = SnapshotParser {
        it.toObject(Birth::class.java)!!.apply { id = it.id }
    }

    override fun constructEntityFromBundle(bundle: Bundle): Birth {
        val calfId = bundle.getString(BaseAddRecordDialogFragment.ADD_DIALOG_CALF, "")
        val dateOfBirth = Date(bundle.getLong(BaseAddRecordDialogFragment.ADD_DIALOG_DATE))
        val note = bundle.getString(BaseAddRecordDialogFragment.ADD_DIALOG_NOTE, "")

        return Birth(calfId, Timestamp(dateOfBirth), getAnimalId(), note, currentUser?.uid)
    }

    override fun validateEntity(entity: Any, listener: (Boolean) -> Unit) {
        animalsCollections.document((entity as Birth).calfId)
            .get()
            .addOnFailureListener {
                alert(R.string.err_validating_calf) { okButton { } }.show()
                listener(false)
                loadingHide()
            }
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    alert(R.string.err_adding_animal_constraint) { okButton { } }.show()
                    loadingHide()
                }
                listener(!snapshot.exists())
            }
    }

    override fun getCollectionReference(): CollectionReference {
        return farmReference.collection(FirestorePath.Collections.BIRTHS)
    }

    override fun getQuery(): Query {
        return getCollectionReference().whereEqualTo(FirestorePath.Birth.MOTHER_ID, getAnimalId())
    }

    override fun addDependentData(entity: Any) {
        (entity as? Birth)?.let {
            val animal = Animal(
                dateOfBirth = it.dateOfBirth,
                gender = getString(R.string.gender_unknown),
                motherId = getAnimalId(),
                createdBy = currentUser?.uid
            )

            animalsCollections.document(it.calfId)
                .set(animal)
                .addOnFailureListener {
                    alert(R.string.err_adding_animal_from_birth) { okButton { } }.show()
                    error { it }
                }
        }
    }

    override fun createHolder(view: View): BirthViewHolder {
        return BirthViewHolder(view,
            { birthToUpdate -> showUpdateDialog(birthToUpdate) },
            { birthToDelete -> showDeleteDialog(birthToDelete) })
    }

    private fun showDeleteDialog(birthToDelete: Birth) {

    }

    private fun showUpdateDialog(birthToUpdate: Birth) {

    }
}
