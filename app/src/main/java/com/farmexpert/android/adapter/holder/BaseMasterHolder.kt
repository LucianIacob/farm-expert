/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/19/19 9:11 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.model.BaseEntity

abstract class BaseMasterHolder<ModelClass : BaseEntity>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun bind(key: String, values: List<ModelClass>)

}
