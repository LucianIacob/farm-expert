/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 8/4/19 10:52 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.transformer

import android.content.res.Resources
import com.farmexpert.android.NavGraphDirections
import com.farmexpert.android.R
import com.farmexpert.android.model.Birth
import com.farmexpert.android.model.Breeding
import com.farmexpert.android.model.Reminder
import com.farmexpert.android.planner.model.PlannerItem
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject

class PlannerDataTransformer {

    companion object {

        fun transformForHeatsContainer(
            querySnapshot: QuerySnapshot,
            resources: Resources
        ): List<PlannerItem> {

            return querySnapshot
                .map { it.toObject<Breeding>() }
                .map {
                    val digitsToShow = resources.getInteger(R.integer.animal_id_digits_to_show)

                    PlannerItem(
                        headline = it.female.takeLast(digitsToShow),
                        reason = resources.getString(R.string.planner_heats_reason),
                        onClickAction = NavGraphDirections.actionGlobalBreedingsDetailFragment(
                            animalId = it.female
                        )
                    )
                }
        }

        fun transformReminders(querySnapshot: QuerySnapshot): List<PlannerItem> {
            return querySnapshot
                .map { it.toObject<Reminder>() }
                .map {
                    PlannerItem(
                        headline = it.details,
                        longClickId = it.id
                    )
                }
        }

        fun transformGestations(
            querySnapshot: QuerySnapshot,
            gestationCtrlDays: Int,
            res: Resources
        ): List<PlannerItem> {

            return querySnapshot
                .map { it.toObject<Breeding>() }
                .map {
                    val digitsToShow = res.getInteger(R.integer.animal_id_digits_to_show)

                    PlannerItem(
                        headline = it.female.takeLast(digitsToShow),
                        reason = res.getString(
                            R.string.planner_gest_ctrl_reason,
                            gestationCtrlDays
                        ),
                        onClickAction = NavGraphDirections.actionGlobalBreedingsDetailFragment(
                            animalId = it.female
                        )
                    )
                }
        }

        fun transformPhysiologicalControl(
            querySnapshot: QuerySnapshot,
            physiologicalPeriod: Int,
            resources: Resources
        ): List<PlannerItem> {

            return querySnapshot
                .map { it.toObject<Birth>() }
                .map {
                    val digitsToShow = resources.getInteger(R.integer.animal_id_digits_to_show)

                    PlannerItem(
                        headline = it.motherId.takeLast(digitsToShow),
                        reason = resources.getString(
                            R.string.planner_physiological_ctrl_reason,
                            physiologicalPeriod
                        ),
                        onClickAction = NavGraphDirections.actionGlobalBirthsDetailFragment(
                            animalId = it.motherId
                        )
                    )
                }
        }

        fun transformVaccine1(
            querySnapshot: QuerySnapshot,
            daysOfFirstVaccine: Int,
            resources: Resources
        ): List<PlannerItem> {

            return querySnapshot
                .map { it.toObject<Birth>() }
                .map {
                    val digitsToShow = resources.getInteger(R.integer.animal_id_digits_to_show)

                    PlannerItem(
                        headline = it.motherId.takeLast(digitsToShow),
                        reason = resources.getString(
                            R.string.planner_first_vaccine_reason,
                            daysOfFirstVaccine
                        ),
                        onClickAction = NavGraphDirections.actionGlobalBirthsDetailFragment(
                            animalId = it.motherId
                        )
                    )
                }
        }

        fun transformBeforeBirthItems(
            querySnapshot: QuerySnapshot,
            daysCount: Int,
            resources: Resources
        ): List<PlannerItem> {

            return querySnapshot.map { it.toObject<Breeding>() }
                .map {
                    val digitsToShow = resources.getInteger(R.integer.animal_id_digits_to_show)

                    PlannerItem(
                        headline = it.female.takeLast(digitsToShow),
                        reason = resources.getString(
                            R.string.planner_before_birth_vaccine_reason,
                            daysCount
                        ),
                        onClickAction = NavGraphDirections.actionGlobalBreedingsDetailFragment(
                            animalId = it.female
                        )
                    )
                }
        }
    }
}
