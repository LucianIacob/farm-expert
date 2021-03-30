/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/10/19 9:16 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.model.BaseEntity

abstract class BaseDetailHolder<ModelClass : BaseEntity>(
    itemView: View,
    val updateListener: (ModelClass) -> Unit,
    val deleteListener: (ModelClass) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(entity: ModelClass)

}
