/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 7/13/19 6:34 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.planner.model.PlannerItem
import kotlinx.android.synthetic.main.item_planner.view.*

class PlannerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(plannerItem: PlannerItem, clickListener: (PlannerItem) -> Unit) = with(view) {
        animalId.text = plannerItem.animalId
        reason.text = plannerItem.reason
    }

}
