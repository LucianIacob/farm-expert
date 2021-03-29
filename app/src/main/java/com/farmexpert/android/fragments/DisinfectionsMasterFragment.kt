/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/20/19 3:43 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import com.farmexpert.android.NavGraphDirections
import com.farmexpert.android.R
import com.farmexpert.android.utils.FirestorePath

/**
 * Created by Lucian Iacob on March 22, 2019.
 */
class DisinfectionsMasterFragment :
    BaseAnimalActionMasterFragment(R.string.dashboard_graph_disinfections) {

    override fun getCollectionRef() =
        farmReference.collection(FirestorePath.Collections.DISINFECTIONS)

    override fun getAnimalClickDirection(animalId: String) =
        NavGraphDirections.actionGlobalDisinfectionsDetailFragment(animalId = animalId)
}