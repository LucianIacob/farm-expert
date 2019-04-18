/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/18/19 10:37 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import com.farmexpert.android.utils.FirestorePath
import com.google.firebase.firestore.CollectionReference

/**
 * Created by Lucian Iacob on March 22, 2019.
 */
class DisinfectionsMasterFragment : BaseAnimalActionMasterFragment() {

    override fun getCollectionRef(): CollectionReference {
        return farmReference.collection(FirestorePath.Collections.DISINFECTIONS)
    }

}