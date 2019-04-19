/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/19/19 9:11 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.view.View
import com.farmexpert.android.R
import com.farmexpert.android.adapter.holder.GraphAnimalActionHolder
import com.farmexpert.android.model.AnimalAction
import com.farmexpert.android.utils.FirestorePath
import com.farmexpert.android.utils.GraphDataTransformer
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.firestore.QuerySnapshot

abstract class BaseAnimalActionMasterFragment :
    BaseMasterFragment<AnimalAction, GraphAnimalActionHolder>() {

    override fun getHolderLayoutRes() = R.layout.item_graph_animal_action

    override val snapshotParser: SnapshotParser<AnimalAction> = SnapshotParser {
        it.toObject(AnimalAction::class.java)!!.apply { id = it.id }
    }

    override fun getFilterField() = FirestorePath.AnimalAction.ACTION_DATE

    override fun getHeaderLayoutRes() = R.layout.graph_animal_actions_header

    override fun createHolder(view: View) = GraphAnimalActionHolder(view)

    override fun transformData(documents: QuerySnapshot?): Map<String, List<AnimalAction>> {
        return GraphDataTransformer.transformDocumentsForAnimalActionsGraph(documents)
    }

}
