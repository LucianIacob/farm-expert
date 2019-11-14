/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/12/19 10:21 AM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.farmexpert.android.R
import com.farmexpert.android.adapter.holder.FarmHolder
import com.farmexpert.android.model.Farm
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

open class FarmSelectorAdapter(
    options: FirestoreRecyclerOptions<Farm>,
    private val listener: (Farm) -> Unit
) : FirestoreRecyclerAdapter<Farm, FarmHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_farm, parent, false)
        return FarmHolder(view)
    }

    override fun onBindViewHolder(farmHolder: FarmHolder, position: Int, farm: Farm) {
        farmHolder.bind(farm)
        farmHolder.itemView.setOnClickListener { listener(farm) }
    }

}
