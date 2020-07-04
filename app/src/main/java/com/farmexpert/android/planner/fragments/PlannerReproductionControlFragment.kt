/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 8/4/19 9:57 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.fragments

import com.crashlytics.android.Crashlytics
import com.farmexpert.android.R
import com.farmexpert.android.planner.model.PlannerContainer
import com.farmexpert.android.planner.model.PlannerItem
import com.farmexpert.android.planner.transformer.PlannerDataTransformer
import com.farmexpert.android.utils.ConfigPickerUtils
import com.farmexpert.android.utils.FirestorePath
import com.farmexpert.android.utils.TimeOfTheDay
import com.farmexpert.android.utils.shift
import com.google.firebase.Timestamp
import org.jetbrains.anko.error
import java.util.*

class PlannerReproductionControlFragment : BasePlannerFragment() {

    private var gestationsArrived = false
    private var birthsArrived = false

    private val containerData = mutableMapOf(
        PLANNER_DATA_GESTATIONS_CONTROL to emptyList<PlannerItem>(),
        PLANNER_DATA_BIRTHS_CONTROL to emptyList()
    )

    override fun getPlannerContainer() = PlannerContainer.REPRODUCTION_CONTROL

    override fun getHeaderText() = getString(R.string.planner_reprod_control_title)

    override fun retrieveDataForDate(date: Date) {
        gestationsArrived = false
        birthsArrived = false

        loadingShow()
        triggerGestationsFetch(date)
        triggerBirthsFetch(date)
    }

    private fun triggerBirthsFetch(date: Date) {
        val daysOfPhysiologicalPeriod = farmTimelinePrefs.getInt(
            getString(R.string.pref_physiological_control_key),
            ConfigPickerUtils.getDefaultValue(
                getString(R.string.pref_physiological_control_key),
                resources
            )
        )
        val startDate = date.shift(days = -daysOfPhysiologicalPeriod, jumpTo = TimeOfTheDay.START)
        val endDate = date.shift(days = -daysOfPhysiologicalPeriod, jumpTo = TimeOfTheDay.END)

        farmReference.collection(FirestorePath.Collections.BIRTHS)
            .whereGreaterThanOrEqualTo(FirestorePath.Birth.DATE_OF_BIRTH, Timestamp(startDate))
            .whereLessThanOrEqualTo(FirestorePath.Birth.DATE_OF_BIRTH, Timestamp(endDate))
            .whereEqualTo(FirestorePath.Birth.LATEST_BIRTH, true)
            .get()
            .addOnSuccessListener {
                birthsArrived = true
                if (this.isAdded) {
                    val transformedData = PlannerDataTransformer.transformPhysiologicalControl(
                        querySnapshot = it,
                        physiologicalPeriod = daysOfPhysiologicalPeriod,
                        resources = resources
                    )

                    dataRetrieved(
                        data = transformedData,
                        mapKey = PLANNER_DATA_BIRTHS_CONTROL,
                        date = date
                    )
                }
            }
            .addOnFailureListener {
                error { it }
                Crashlytics.logException(it)
            }
    }

    private fun triggerGestationsFetch(date: Date) {
        val daysOfGestationPeriod = farmTimelinePrefs.getInt(
            getString(R.string.pref_gestation_key),
            ConfigPickerUtils.getDefaultValue(
                getString(R.string.pref_gestation_key),
                resources
            )
        )
        val startDate = date.shift(days = -daysOfGestationPeriod, jumpTo = TimeOfTheDay.START)
        val endDate = date.shift(days = -daysOfGestationPeriod, jumpTo = TimeOfTheDay.END)

        farmReference.collection(FirestorePath.Collections.BREEDINGS)
            .whereGreaterThanOrEqualTo(FirestorePath.Breeding.ACTION_DATE, Timestamp(startDate))
            .whereLessThanOrEqualTo(FirestorePath.Breeding.ACTION_DATE, Timestamp(endDate))
            .whereEqualTo(FirestorePath.Breeding.LATEST_BREEDING, true)
            .get()
            .addOnSuccessListener {
                gestationsArrived = true
                if (this.isAdded) {
                    dataRetrieved(
                        data = PlannerDataTransformer.transformGestations(
                            querySnapshot = it,
                            gestationCtrlDays = daysOfGestationPeriod,
                            res = resources
                        ),
                        mapKey = PLANNER_DATA_GESTATIONS_CONTROL,
                        date = date
                    )
                }
            }
            .addOnFailureListener {
                error { it }
                Crashlytics.logException(it)
            }
    }

    private fun dataRetrieved(data: List<PlannerItem>, mapKey: String, date: Date) {
        containerData[mapKey] = data

        if (birthsArrived && gestationsArrived) {
            val combinedData = (containerData[PLANNER_DATA_GESTATIONS_CONTROL] as List<PlannerItem>)
                .plus((containerData[PLANNER_DATA_BIRTHS_CONTROL] as List<PlannerItem>))

            dataRetrievedSuccessfully(combinedData, PLANNER_DATA_ANIMALS)
            super.retrieveDataForDate(date)
        }
    }

    companion object {
        const val PLANNER_DATA_GESTATIONS_CONTROL = "com.farmexpert.android.PlannerData.Gestations"
        const val PLANNER_DATA_BIRTHS_CONTROL = "com.farmexpert.android.PlannerData.Births"
    }
}
