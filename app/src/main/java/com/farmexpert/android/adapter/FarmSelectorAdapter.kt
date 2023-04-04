/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter

import android.view.ViewGroup
import com.farmexpert.android.R
import com.farmexpert.android.adapter.holder.FarmHolder
import com.farmexpert.android.model.Farm
import com.farmexpert.android.utils.inflate
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

open class FarmSelectorAdapter(
    options: FirestoreRecyclerOptions<Farm>,
    private val listener: (Farm) -> Unit
) : FirestoreRecyclerAdapter<Farm, FarmHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FarmHolder(parent.inflate(R.layout.item_farm))

    override fun onBindViewHolder(farmHolder: FarmHolder, position: Int, farm: Farm) {
        farmHolder.bind(farm)
        farmHolder.itemView.setOnClickListener { listener(farm) }
    }
}
