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
import com.farmexpert.android.model.BaseEntity

abstract class BaseMasterHolder<ModelClass : BaseEntity>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun bind(key: String, values: List<ModelClass>)

}
