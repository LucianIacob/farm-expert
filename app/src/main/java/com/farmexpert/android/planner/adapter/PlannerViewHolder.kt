/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 8/4/19 10:52 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.adapter

import android.view.View
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.planner.model.PlannerItem
import kotlinx.android.synthetic.main.item_planner.view.*

class PlannerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(
        plannerItem: PlannerItem,
        clickHandler: (NavDirections) -> Unit,
        longClickHandler: (String) -> Unit
    ) = with(view) {
        headline.text = plannerItem.headline
        plannerItem.reason?.let {
            reason.text = it
            reason.visibility = View.VISIBLE
        } ?: run { reason.visibility = View.GONE }

        view.setOnClickListener { _ -> plannerItem.onClickAction?.let { clickHandler(it) } }
        view.setOnLongClickListener { _ ->
            plannerItem.longClickId?.let { longClickHandler(it) }
            true
        }
    }

}
