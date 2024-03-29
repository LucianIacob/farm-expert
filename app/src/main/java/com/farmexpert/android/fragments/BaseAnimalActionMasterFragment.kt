/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.farmexpert.android.R
import com.farmexpert.android.adapter.holder.GraphAnimalActionHolder
import com.farmexpert.android.model.AnimalAction
import com.farmexpert.android.utils.FirestorePath
import com.farmexpert.android.utils.GraphDataTransformer
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject

abstract class BaseAnimalActionMasterFragment(title: Int) :
    BaseMasterFragment<AnimalAction, GraphAnimalActionHolder>(title) {

    override val holderLayoutRes = R.layout.item_graph_animal_action

    override val snapshotParser: SnapshotParser<AnimalAction> = SnapshotParser {
        it.toObject<AnimalAction>()!!.apply { id = it.id }
    }

    override val filterField = FirestorePath.AnimalAction.ACTION_DATE

    override val headerLayoutRes = R.layout.graph_animal_actions_header

    override fun createHolder(view: View) = GraphAnimalActionHolder(
        view = view,
        animalIdClick = { animalId -> handleAnimalClicked(animalId) }
    )

    private fun handleAnimalClicked(animalId: String) {
        navigationListener.invoke()
        NavHostFragment.findNavController(this)
            .navigate(getAnimalClickDirection(animalId))
    }

    abstract fun getAnimalClickDirection(animalId: String): NavDirections

    override fun transformData(documents: QuerySnapshot?): Map<String, List<AnimalAction>> =
        GraphDataTransformer.transformDocumentsForAnimalActionsGraph(documents)
}
