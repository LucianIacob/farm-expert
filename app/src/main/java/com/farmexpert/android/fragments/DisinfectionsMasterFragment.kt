/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/19/19 10:24 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import com.farmexpert.android.R
import com.farmexpert.android.utils.FirestorePath
import com.google.firebase.firestore.CollectionReference

/**
 * Created by Lucian Iacob on March 22, 2019.
 */
class DisinfectionsMasterFragment : BaseAnimalActionMasterFragment() {

    override fun getCollectionRef(): CollectionReference {
        return farmReference.collection(FirestorePath.Collections.DISINFECTIONS)
    }

    override fun getTitle(): String = getString(R.string.dashboard_graph_disinfections)
}