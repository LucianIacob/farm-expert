/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 8/4/19 10:52 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.adapter

import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.R
import com.farmexpert.android.planner.model.PlannerItem
import com.farmexpert.android.utils.inflate

class PlannerAdapter(
    private val clickListener: (NavDirections) -> Unit,
    private val longClickListener: (String) -> Unit
) : RecyclerView.Adapter<PlannerViewHolder>() {

    var data: List<PlannerItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlannerViewHolder(parent.inflate(R.layout.item_planner))

    override fun getItemCount() = data.size

    override fun onBindViewHolder(plannerViewHolder: PlannerViewHolder, position: Int) {
        plannerViewHolder.bind(data[position], clickListener, longClickListener)
    }
}
