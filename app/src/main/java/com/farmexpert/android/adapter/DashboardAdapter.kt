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
import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.R
import com.farmexpert.android.adapter.holder.DashboardItemHolder
import com.farmexpert.android.model.DashboardItem
import com.farmexpert.android.utils.inflate

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, February 13, 2018.
 */
class DashboardAdapter(
    private val data: Array<DashboardItem>,
    private val listener: (DashboardItem) -> Unit
) : RecyclerView.Adapter<DashboardItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardItemHolder =
        DashboardItemHolder(parent.inflate(R.layout.item_dashboard))

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: DashboardItemHolder, position: Int) {
        holder.bind(data[position], listener)
    }
}