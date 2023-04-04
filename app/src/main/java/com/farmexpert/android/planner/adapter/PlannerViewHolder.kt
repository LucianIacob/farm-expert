/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.adapter

import android.view.View
import androidx.core.view.isVisible
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
            reason.isVisible = true
        } ?: run { reason.isVisible = false }

        view.setOnClickListener { _ -> plannerItem.onClickAction?.let { clickHandler(it) } }
        view.setOnLongClickListener { _ ->
            plannerItem.longClickId?.let { longClickHandler(it) }
            true
        }
    }

}
