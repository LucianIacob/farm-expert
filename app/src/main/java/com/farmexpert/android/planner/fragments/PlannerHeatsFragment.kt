/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 8/4/19 10:12 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.fragments

import com.farmexpert.android.R
import com.farmexpert.android.planner.model.PlannerContainer
import com.farmexpert.android.planner.transformer.PlannerDataTransformer
import com.farmexpert.android.utils.*
import com.google.firebase.Timestamp
import java.util.*

class PlannerHeatsFragment : BasePlannerFragment() {

    override fun getHeaderText() = getString(R.string.planner_heats_title)

    override fun getPlannerContainer() = PlannerContainer.HEAT_CYCLE

    override fun retrieveDataForDate(date: Date) {
        val heatCycleStart = farmTimelinePrefs.getInt(
            getString(R.string.pref_heating_start_key),
            ConfigPickerUtils.getDefaultValue(getString(R.string.pref_heating_start_key), resources)
        )

        val heatCycleEnd = farmTimelinePrefs.getInt(
            getString(R.string.pref_heating_end_key),
            ConfigPickerUtils.getDefaultValue(getString(R.string.pref_heating_end_key), resources)
        )

        val startDate = date.shift(days = -heatCycleEnd, jumpTo = TimeOfTheDay.START)
        val endDate = date.shift(days = -heatCycleStart, jumpTo = TimeOfTheDay.END)

        loadingShow()
        farmReference.collection(FirestorePath.Collections.BREEDINGS)
            .whereGreaterThanOrEqualTo(FirestorePath.Breeding.ACTION_DATE, Timestamp(startDate))
            .whereLessThanOrEqualTo(FirestorePath.Breeding.ACTION_DATE, Timestamp(endDate))
            .whereEqualTo(FirestorePath.Breeding.LATEST_BREEDING, true)
            .get()
            .addOnSuccessListener {
                if (this.isAdded) {
                    dataRetrievedSuccessfully(
                        plannerList = PlannerDataTransformer.transformForHeatsContainer(
                            querySnapshot = it,
                            resources = resources
                        ),
                        dataType = PLANNER_DATA_ANIMALS
                    )
                }
            }
            .addLoggableFailureListener()
            .addOnCompleteListener { super.retrieveDataForDate(date) }
    }
}
