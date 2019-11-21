/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/19/19 9:11 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.adapter.holder.BaseMasterHolder
import com.farmexpert.android.model.BaseEntity

open class GraphAdapter<ModelClass : BaseEntity, HolderClass : BaseMasterHolder<ModelClass>>(
    @LayoutRes val layoutResId: Int,
    private val createHolderClass: (view: View) -> HolderClass
) : RecyclerView.Adapter<HolderClass>() {

    private var keys: Set<String> = setOf()

    var data: Map<String, List<ModelClass>> = mapOf()
        set(value) {
            field = value
            keys = value.keys
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderClass {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return createHolderClass(view)
    }

    override fun onBindViewHolder(holder: HolderClass, position: Int) {
        keys.elementAtOrNull(position)?.let { animalId ->
            val value = data[animalId]
            value?.let { holder.bind(key = animalId, values = value) }
        }
    }

}
