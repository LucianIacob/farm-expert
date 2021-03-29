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
import com.farmexpert.android.adapter.holder.GraphBreedingViewHolder
import com.farmexpert.android.model.Breeding
import com.farmexpert.android.utils.FirestorePath
import com.farmexpert.android.utils.GraphDataTransformer
import com.farmexpert.android.utils.takeIfNotBlank
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject

/**
 * Created by Lucian Iacob on March 22, 2019.
 */
class BreedingsMasterFragment :
    BaseMasterFragment<Breeding, GraphBreedingViewHolder>(R.string.dashboard_graph_breedings) {

    override val holderLayoutRes = R.layout.item_graph_breeding
    override val headerLayoutRes = R.layout.graph_breedings_header

    override val snapshotParser: SnapshotParser<Breeding> = SnapshotParser {
        it.toObject<Breeding>()!!.apply { id = it.id }
    }

    override val filterField = FirestorePath.Breeding.ACTION_DATE

    override fun getCollectionRef() =
        farmReference.collection(FirestorePath.Collections.BREEDINGS).let {
            return@let if (resources.getBoolean(R.bool.graph_latest_breedings_only)) {
                it.whereEqualTo(FirestorePath.Breeding.LATEST_BREEDING, true)
            } else it
        }

    override fun createHolder(view: View) =
        GraphBreedingViewHolder(
            view = view,
            femaleIdClick = { femaleId -> handleFemaleClicked(femaleId) },
            maleIdClick = { maleId -> handleMaleClicked(maleId) }
        )

    private fun handleMaleClicked(maleId: String) {
        maleId.takeIfNotBlank()?.let {
            navigationListener.invoke()
            NavHostFragment
                .findNavController(this)
                .navigate(NavGraphDirections.actionGlobalAnimalDetailFragment(animalId = maleId))
        }
    }

    private fun handleFemaleClicked(femaleId: String) {
        navigationListener.invoke()
        NavHostFragment.findNavController(this)
            .navigate(NavGraphDirections.actionGlobalBreedingsDetailFragment(animalId = femaleId))
    }

    override fun transformData(documents: QuerySnapshot?): Map<String, List<Breeding>> =
        GraphDataTransformer.transformDocumentsForBreedingsGraph(documents)
}