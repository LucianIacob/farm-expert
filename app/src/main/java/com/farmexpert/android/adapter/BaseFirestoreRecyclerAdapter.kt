/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter

import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.model.BaseEntity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

abstract class BaseFirestoreRecyclerAdapter<ModelClass : BaseEntity, HolderClass : RecyclerView.ViewHolder>(
    options: FirestoreRecyclerOptions<ModelClass>
) : FirestoreRecyclerAdapter<ModelClass, HolderClass>(options)