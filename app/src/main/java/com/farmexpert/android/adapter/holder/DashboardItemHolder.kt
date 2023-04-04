/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter.holder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.model.DashboardItem
import kotlinx.android.synthetic.main.item_dashboard.view.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, February 13, 2018.
 */
class DashboardItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: DashboardItem, listener: (DashboardItem) -> Unit) = with(itemView) {
        item_dashboard_icon.setImageDrawable(ContextCompat.getDrawable(context, item.iconId))
        item_dashboard_title.text = context.getString(item.titleId)

        setOnClickListener { listener(item) }
    }
}