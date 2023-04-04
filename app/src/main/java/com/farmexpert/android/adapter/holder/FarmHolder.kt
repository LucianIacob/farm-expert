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
import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.R
import com.farmexpert.android.model.Farm
import com.farmexpert.android.utils.asDisplayable
import kotlinx.android.synthetic.main.item_farm.view.*

class FarmHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(farm: Farm) {
        itemView.farmName.text = farm.name
        itemView.createdAt.text =
            itemView.resources.getString(R.string.farm_creation_date, farm.createdOn.asDisplayable())
    }
}
