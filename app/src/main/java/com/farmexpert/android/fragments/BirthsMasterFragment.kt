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
import androidx.navigation.fragment.NavHostFragment
import com.farmexpert.android.NavGraphDirections
import com.farmexpert.android.R
import com.farmexpert.android.adapter.holder.GraphBirthViewHolder
import com.farmexpert.android.model.Birth
import com.farmexpert.android.utils.FirestorePath
import com.farmexpert.android.utils.GraphDataTransformer
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject

/**
 * Created by Lucian Iacob on March 22, 2019.
 */
class BirthsMasterFragment :
    BaseMasterFragment<Birth, GraphBirthViewHolder>(R.string.dashboard_graph_births) {

    override val headerLayoutRes = R.layout.graph_births_header
    override val filterField = FirestorePath.Birth.DATE_OF_BIRTH
    override val holderLayoutRes = R.layout.item_graph_birth

    override val snapshotParser: SnapshotParser<Birth> = SnapshotParser {
        it.toObject<Birth>()!!.apply { id = it.id }
    }

    override fun createHolder(view: View) = GraphBirthViewHolder(
        itemView = view,
        motherIdClick = { motherId -> handleMotherClicked(motherId) },
        calfIdClick = { calfId -> handleCalfClicked(calfId) }
    )

    private fun handleCalfClicked(calfId: String) {
        navigationListener.invoke()
        NavHostFragment.findNavController(this)
            .navigate(NavGraphDirections.actionGlobalAnimalDetailFragment(animalId = calfId))
    }

    private fun handleMotherClicked(motherId: String) {
        navigationListener.invoke()
        NavHostFragment.findNavController(this)
            .navigate(NavGraphDirections.actionGlobalBirthsDetailFragment(animalId = motherId))
    }

    override fun getCollectionRef() =
        farmReference.collection(FirestorePath.Collections.BIRTHS).let {
            return@let if (resources.getBoolean(R.bool.graph_latest_births_only)) {
                it.whereEqualTo(FirestorePath.Birth.LATEST_BIRTH, true)
            } else it
        }

    override fun transformData(documents: QuerySnapshot?): Map<String, List<Birth>> =
        GraphDataTransformer.transformDocumentsForBirthsGraph(documents)
}