/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/9/19 9:25 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.farmexpert.android.adapter.holder.BaseDetailHolder
import com.farmexpert.android.model.BaseEntity
import com.farmexpert.android.utils.inflate
import com.firebase.ui.firestore.FirestoreRecyclerOptions

open class AnimalActionsAdapter<ModelClass : BaseEntity, HolderClass : BaseDetailHolder<ModelClass>>(
    options: FirestoreRecyclerOptions<ModelClass>,
    @LayoutRes val layoutResId: Int,
    private val createHolderClass: (view: View) -> HolderClass
) : BaseFirestoreRecyclerAdapter<ModelClass, HolderClass>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderClass =
        createHolderClass(parent.inflate(layoutResId))

    override fun onBindViewHolder(
        holderClass: HolderClass,
        position: Int,
        modelClass: ModelClass
    ) {
        holderClass.bind(modelClass)
    }

}
