/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 7/13/19 6:34 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.R
import com.farmexpert.android.planner.model.PlannerItem
import com.farmexpert.android.utils.inflate

class PlannerAdapter(private val clickListener: (PlannerItem) -> Unit) :
    RecyclerView.Adapter<PlannerViewHolder>() {

    var data: List<PlannerItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlannerViewHolder {
        return PlannerViewHolder(parent.inflate(R.layout.item_planner))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(plannerViewHolder: PlannerViewHolder, position: Int) {
        plannerViewHolder.bind(data[position], clickListener)
    }

}
