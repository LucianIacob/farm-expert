/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 7/13/19 6:34 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.transformer

import android.content.res.Resources
import com.farmexpert.android.R
import com.farmexpert.android.model.Breeding
import com.farmexpert.android.planner.model.PlannerItem
import com.google.firebase.firestore.QuerySnapshot

class PlannerDataTransformer {

    companion object {

        fun transformForHeatsContainer(
            querySnapshot: QuerySnapshot?,
            resources: Resources
        ): List<PlannerItem>? {
            return querySnapshot?.map { it.toObject(Breeding::class.java) }
                ?.groupBy { it.female }
                ?.mapValues { it.value.maxBy { breeding -> breeding.actionDate } }
                ?.map {
                    PlannerItem(
                        animalId = it.key,
                        reason = resources.getString(R.string.planner_heats_reason)
                    )
                }

        }

    }

}
