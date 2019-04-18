/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/18/19 9:35 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.farmexpert.android.adapter.holder.BaseMasterHolder
import com.farmexpert.android.model.BaseEntity
import com.firebase.ui.firestore.FirestoreRecyclerOptions

open class GraphAdapter<ModelClass : BaseEntity, HolderClass : BaseMasterHolder<ModelClass>>(
    options: FirestoreRecyclerOptions<ModelClass>,
    @LayoutRes val layoutResId: Int,
    private val createHolderClass: (view: View) -> HolderClass
) : BaseFirestoreRecyclerAdapter<ModelClass, HolderClass>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderClass {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return createHolderClass(view)
    }

    override fun onBindViewHolder(holderClass: HolderClass, p1: Int, modelClass: ModelClass) {
        holderClass.bind(modelClass)
    }

}
