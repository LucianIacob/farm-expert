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
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot

/**
 * Created by Lucian Iacob on March 22, 2019.
 */
class BreedingsMasterFragment : BaseMasterFragment<Breeding, GraphBreedingViewHolder>() {

    override val snapshotParser: SnapshotParser<Breeding> = SnapshotParser {
        it.toObject(Breeding::class.java)!!.apply { id = it.id }
    }

    override fun getFilterField() = FirestorePath.Breeding.ACTION_DATE

    override fun getCollectionRef(): CollectionReference {
        return farmReference.collection(FirestorePath.Collections.BREEDINGS)
    }

    override fun getHolderLayoutRes() = R.layout.item_graph_breeding

    override fun getHeaderLayoutRes() = R.layout.graph_breedings_header

    override fun createHolder(view: View) = GraphBreedingViewHolder(
        view = view,
        femaleIdClick = { femaleId -> handleFemaleClicked(femaleId) },
        maleIdClick = { maleId -> handleMaleClicked(maleId) }
    )

    private fun handleMaleClicked(maleId: String) {
        val direction = NavGraphDirections.actionGlobalAnimalDetailFragment(animalId = maleId)
        NavHostFragment.findNavController(this).navigate(direction)
    }

    private fun handleFemaleClicked(femaleId: String) {
        val direction = NavGraphDirections.actionGlobalBreedingsDetailFragment(animalId = femaleId)
        NavHostFragment.findNavController(this).navigate(direction)
    }

    override fun transformData(documents: QuerySnapshot?): Map<String, List<Breeding>> {
        return GraphDataTransformer.transformDocumentsForBreedingsGraph(documents)
    }

    override fun getTitle(): String = getString(R.string.dashboard_graph_breedings)

}