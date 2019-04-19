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
import com.farmexpert.android.adapter.holder.GraphBirthViewHolder
import com.farmexpert.android.model.Birth
import com.farmexpert.android.utils.FirestorePath
import com.farmexpert.android.utils.GraphDataTransformer
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot

/**
 * Created by Lucian Iacob on March 22, 2019.
 */
class BirthsMasterFragment : BaseMasterFragment<Birth, GraphBirthViewHolder>() {

    override fun getHolderLayoutRes() = R.layout.item_graph_birth

    override fun createHolder(view: View) = GraphBirthViewHolder(view)

    override fun getHeaderLayoutRes() = R.layout.graph_births_header

    override fun getFilterField() = FirestorePath.Birth.DATE_OF_BIRTH

    override fun getCollectionRef(): CollectionReference {
        return farmReference.collection(FirestorePath.Collections.BIRTHS)
    }

    override val snapshotParser: SnapshotParser<Birth> = SnapshotParser {
        it.toObject(Birth::class.java)!!.apply { id = it.id }
    }

    override fun transformData(documents: QuerySnapshot?): Map<String, List<Birth>> {
        return GraphDataTransformer.transformDocumentsForBirthsGraph(documents)
    }
}