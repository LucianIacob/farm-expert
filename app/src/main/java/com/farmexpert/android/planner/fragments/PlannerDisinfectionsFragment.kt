/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.fragments

import com.farmexpert.android.R
import com.farmexpert.android.planner.model.PlannerContainer
import com.farmexpert.android.planner.transformer.PlannerDataTransformer
import com.farmexpert.android.utils.*
import com.google.firebase.Timestamp
import java.util.*

class PlannerDisinfectionsFragment : BasePlannerFragment(R.string.planner_disinfections_title) {

    override val getPlannerContainer = PlannerContainer.DISINFECTION

    override fun retrieveDataForDate(date: Date) {
        val daysOfDisinfection = farmTimelinePrefs.getInt(
            getString(R.string.pref_disinfection_key),
            ConfigPickerUtils.getDefaultValue(
                getString(R.string.pref_disinfection_key),
                resources
            )
        )

        val startDate = date.shift(days = daysOfDisinfection, jumpTo = TimeOfTheDay.START)
        val endDate = date.shift(days = daysOfDisinfection, jumpTo = TimeOfTheDay.END)

        farmReference.collection(FirestorePath.Collections.BREEDINGS)
            .whereGreaterThanOrEqualTo(FirestorePath.Breeding.EXPECTED_BIRTH, Timestamp(startDate))
            .whereLessThanOrEqualTo(FirestorePath.Breeding.EXPECTED_BIRTH, Timestamp(endDate))
            .whereEqualTo(FirestorePath.Breeding.LATEST_BREEDING, true)
            .get()
            .addOnSuccessListener {
                if (isAdded) {
                    dataRetrievedSuccessfully(
                        plannerList = PlannerDataTransformer.transformBeforeBirthItems(
                            querySnapshot = it,
                            daysCount = daysOfDisinfection,
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
