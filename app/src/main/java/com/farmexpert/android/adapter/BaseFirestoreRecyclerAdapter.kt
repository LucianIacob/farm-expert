/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/9/19 9:25 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter

import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.model.BaseEntity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

abstract class BaseFirestoreRecyclerAdapter<ModelClass : BaseEntity, HolderClass : RecyclerView.ViewHolder>(
    options: FirestoreRecyclerOptions<ModelClass>
) : FirestoreRecyclerAdapter<ModelClass, HolderClass>(options) {

    private var shouldListen: Boolean = false

    fun readyForListening() {
        shouldListen = true
        startListening()
    }

    override fun startListening() {
        if (shouldListen) {
            super.startListening()
        }
    }

}