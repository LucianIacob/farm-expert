/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/20/19 3:43 PM.
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
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject

/**
 * Created by Lucian Iacob on March 22, 2019.
 */
class BirthsMasterFragment : BaseMasterFragment<Birth, GraphBirthViewHolder>() {

    override fun getHolderLayoutRes() = R.layout.item_graph_birth

    override fun createHolder(view: View) = GraphBirthViewHolder(
        itemView = view,
        motherIdClick = { motherId -> handleMotherClicked(motherId) },
        calfIdClick = { calfId -> handleCalfClicked(calfId) }
    )

    private fun handleCalfClicked(calfId: String) {
        val direction = NavGraphDirections.actionGlobalAnimalDetailFragment(animalId = calfId)
        NavHostFragment.findNavController(this).navigate(direction)
    }

    private fun handleMotherClicked(motherId: String) {
        val direction = NavGraphDirections.actionGlobalBirthsDetailFragment(animalId = motherId)
        NavHostFragment.findNavController(this).navigate(direction)
    }

    override fun getHeaderLayoutRes() = R.layout.graph_births_header

    override fun getFilterField() = FirestorePath.Birth.DATE_OF_BIRTH

    override fun getCollectionRef(): Query {
        return farmReference.collection(FirestorePath.Collections.BIRTHS).let {
            return@let if (resources.getBoolean(R.bool.graph_latest_births_only)) {
                it.whereEqualTo(FirestorePath.Birth.LATEST_BIRTH, true)
            } else {
                it
            }
        }
    }

    override val snapshotParser: SnapshotParser<Birth> = SnapshotParser {
        it.toObject<Birth>()!!.apply { id = it.id }
    }

    override fun transformData(documents: QuerySnapshot?): Map<String, List<Birth>> {
        return GraphDataTransformer.transformDocumentsForBirthsGraph(documents)
    }

    override fun getTitle(): String = getString(R.string.dashboard_graph_births)
}