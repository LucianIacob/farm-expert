/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 8/4/19 10:12 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.transformer

import android.content.res.Resources
import com.farmexpert.android.R
import com.farmexpert.android.model.Birth
import com.farmexpert.android.model.Breeding
import com.farmexpert.android.model.Reminder
import com.farmexpert.android.planner.model.PlannerItem
import com.google.firebase.firestore.QuerySnapshot

class PlannerDataTransformer {

    companion object {

        fun transformForHeatsContainer(
            querySnapshot: QuerySnapshot,
            resources: Resources
        ): List<PlannerItem> {
            return querySnapshot.map {
                val breeding = it.toObject(Breeding::class.java)
                PlannerItem(
                    headline = breeding.female,
                    reason = resources.getString(R.string.planner_heats_reason)
                )
            }
        }

        fun transformReminders(querySnapshot: QuerySnapshot): List<PlannerItem> {
            return querySnapshot.map { it.toObject(Reminder::class.java) }
                .map { PlannerItem(headline = it.details) }
        }

        fun transformGestations(
            querySnapshot: QuerySnapshot,
            gestationCtrlDays: Int,
            res: Resources
        ): List<PlannerItem> {
            return querySnapshot.map {
                val breeding = it.toObject(Breeding::class.java)
                PlannerItem(
                    headline = breeding.female,
                    reason = res.getString(R.string.planner_gest_ctrl_reason, gestationCtrlDays)
                )
            }
        }

        fun transformPhysiologicalControl(
            querySnapshot: QuerySnapshot,
            physiologicalPeriod: Int,
            resources: Resources
        ): List<PlannerItem> {
            return querySnapshot.map {
                val birth = it.toObject(Birth::class.java)
                PlannerItem(
                    headline = birth.motherId,
                    reason = resources.getString(
                        R.string.planner_physiological_ctrl_reason,
                        physiologicalPeriod
                    )
                )
            }
        }

        fun transformVaccine1(
            querySnapshot: QuerySnapshot,
            daysOfFirstVaccine: Int,
            resources: Resources
        ): List<PlannerItem> {
            return querySnapshot.map {
                val birth = it.toObject(Birth::class.java)
                PlannerItem(
                    headline = birth.motherId,
                    reason = resources.getString(
                        R.string.planner_first_vaccine_reason,
                        daysOfFirstVaccine
                    )
                )
            }
        }

        fun transformBeforeBirthItems(
            querySnapshot: QuerySnapshot,
            daysCount: Int,
            resources: Resources
        ): List<PlannerItem> {
            return querySnapshot.map {
                val breeding = it.toObject(Breeding::class.java)
                PlannerItem(
                    headline = breeding.female,
                    reason = resources.getString(
                        R.string.planner_before_birth_vaccine_reason,
                        daysCount
                    )
                )
            }
        }
    }
}
