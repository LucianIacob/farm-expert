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
import com.farmexpert.android.planner.model.PlannerItem
import com.farmexpert.android.planner.transformer.PlannerDataTransformer
import com.farmexpert.android.utils.*
import com.google.firebase.Timestamp
import java.util.*

class PlannerVaccinationsFragment : BasePlannerFragment(R.string.planner_vaccinations_title) {

    override val getPlannerContainer = PlannerContainer.VACCINATIONS

    private var vaccine1Arrived = false
    private var vaccine2Arrived = false
    private var vaccine3Arrived = false
    private var vaccine4Arrived = false

    private val containerData = mutableMapOf(
        PLANNER_DATA_VACCINE_1 to emptyList<PlannerItem>(),
        PLANNER_DATA_VACCINE_2 to emptyList(),
        PLANNER_DATA_VACCINE_3 to emptyList(),
        PLANNER_DATA_VACCINE_4 to emptyList()
    )

    override fun retrieveDataForDate(date: Date) {
        vaccine1Arrived = false
        vaccine2Arrived = false
        vaccine3Arrived = false
        vaccine4Arrived = false

        loadingShow()
        fetchFirstDataSet(date)
        fetchSecondDataSet(date)
        fetchThirdDataSet(date)
        fetchForthDataSet(date)
    }

    private fun fetchFirstDataSet(date: Date) {
        val daysOfFirstVaccine = farmTimelinePrefs.getInt(
            getString(R.string.pref_vaccin_after_birth_key),
            ConfigPickerUtils.getDefaultValue(
                getString(R.string.pref_vaccin_after_birth_key),
                resources
            )
        )

        val startDate = date.shift(days = -daysOfFirstVaccine, jumpTo = TimeOfTheDay.START)
        val endDate = date.shift(days = -daysOfFirstVaccine, jumpTo = TimeOfTheDay.END)

        farmReference.collection(FirestorePath.Collections.BIRTHS)
            .whereGreaterThanOrEqualTo(FirestorePath.Birth.DATE_OF_BIRTH, Timestamp(startDate))
            .whereLessThanOrEqualTo(FirestorePath.Birth.DATE_OF_BIRTH, Timestamp(endDate))
            .whereEqualTo(FirestorePath.Birth.LATEST_BIRTH, true)
            .get()
            .addLoggableFailureListener()
            .addOnSuccessListener {
                vaccine1Arrived = true
                if (this.isAdded) {
                    dataRetrieved(
                        items = PlannerDataTransformer.transformVaccine1(
                            querySnapshot = it,
                            daysOfFirstVaccine = daysOfFirstVaccine,
                            resources = resources
                        ),
                        mapKey = PLANNER_DATA_VACCINE_1,
                        date = date
                    )
                }
            }
    }

    private fun fetchSecondDataSet(date: Date) {
        val daysOfSecondVaccine = farmTimelinePrefs.getInt(
            getString(R.string.pref_vaccin1_before_birth_key),
            ConfigPickerUtils.getDefaultValue(
                getString(R.string.pref_vaccin1_before_birth_key),
                resources
            )
        )

        val startDate = date.shift(days = daysOfSecondVaccine, jumpTo = TimeOfTheDay.START)
        val endDate = date.shift(days = daysOfSecondVaccine, jumpTo = TimeOfTheDay.END)

        farmReference.collection(FirestorePath.Collections.BREEDINGS)
            .whereGreaterThanOrEqualTo(FirestorePath.Breeding.EXPECTED_BIRTH, Timestamp(startDate))
            .whereLessThanOrEqualTo(FirestorePath.Breeding.EXPECTED_BIRTH, Timestamp(endDate))
            .whereEqualTo(FirestorePath.Breeding.LATEST_BREEDING, true)
            .get()
            .addLoggableFailureListener()
            .addOnSuccessListener {
                vaccine2Arrived = true
                if (this.isAdded) {
                    dataRetrieved(
                        items = PlannerDataTransformer
                            .transformBeforeBirthItems(
                                querySnapshot = it,
                                daysCount = daysOfSecondVaccine,
                                resources = resources
                            ),
                        mapKey = PLANNER_DATA_VACCINE_2,
                        date = date
                    )
                }
            }
    }

    private fun fetchThirdDataSet(date: Date) {
        val daysOfThirdVaccine = farmTimelinePrefs.getInt(
            getString(R.string.pref_vaccin2_before_birth_key),
            ConfigPickerUtils.getDefaultValue(
                getString(R.string.pref_vaccin2_before_birth_key),
                resources
            )
        )

        val startDate = date.shift(days = daysOfThirdVaccine, jumpTo = TimeOfTheDay.START)
        val endDate = date.shift(days = daysOfThirdVaccine, jumpTo = TimeOfTheDay.END)

        farmReference.collection(FirestorePath.Collections.BREEDINGS)
            .whereGreaterThanOrEqualTo(FirestorePath.Breeding.EXPECTED_BIRTH, Timestamp(startDate))
            .whereLessThanOrEqualTo(FirestorePath.Breeding.EXPECTED_BIRTH, Timestamp(endDate))
            .whereEqualTo(FirestorePath.Breeding.LATEST_BREEDING, true)
            .get()
            .addLoggableFailureListener()
            .addOnSuccessListener {
                vaccine3Arrived = true
                if (this.isAdded) {
                    dataRetrieved(
                        items = PlannerDataTransformer.transformBeforeBirthItems(
                            querySnapshot = it,
                            daysCount = daysOfThirdVaccine,
                            resources = resources
                        ),
                        mapKey = PLANNER_DATA_VACCINE_3,
                        date = date
                    )
                }
            }
    }

    private fun fetchForthDataSet(date: Date) {
        val daysOfForthVaccine = farmTimelinePrefs.getInt(
            getString(R.string.pref_vaccin3_before_birth_key),
            ConfigPickerUtils.getDefaultValue(
                getString(R.string.pref_vaccin3_before_birth_key),
                resources
            )
        )

        val startDate = date.shift(days = daysOfForthVaccine, jumpTo = TimeOfTheDay.START)
        val endDate = date.shift(days = daysOfForthVaccine, jumpTo = TimeOfTheDay.END)

        farmReference.collection(FirestorePath.Collections.BREEDINGS)
            .whereGreaterThanOrEqualTo(FirestorePath.Breeding.EXPECTED_BIRTH, Timestamp(startDate))
            .whereLessThanOrEqualTo(FirestorePath.Breeding.EXPECTED_BIRTH, Timestamp(endDate))
            .whereEqualTo(FirestorePath.Breeding.LATEST_BREEDING, true)
            .get()
            .addLoggableFailureListener()
            .addOnSuccessListener {
                vaccine4Arrived = true
                if (this.isAdded) {
                    dataRetrieved(
                        items = PlannerDataTransformer.transformBeforeBirthItems(
                            querySnapshot = it,
                            daysCount = daysOfForthVaccine,
                            resources = resources
                        ),
                        mapKey = PLANNER_DATA_VACCINE_4,
                        date = date
                    )
                }
            }
    }

    private fun dataRetrieved(items: List<PlannerItem>, mapKey: String, date: Date) {
        containerData[mapKey] = items

        if (vaccine1Arrived && vaccine2Arrived && vaccine3Arrived && vaccine4Arrived) {
            val combinedData = (containerData[PLANNER_DATA_VACCINE_1] as List<PlannerItem>)
                .plus((containerData[PLANNER_DATA_VACCINE_2] as List<PlannerItem>))
                .plus(containerData[PLANNER_DATA_VACCINE_3] as List<PlannerItem>)
                .plus(containerData[PLANNER_DATA_VACCINE_4] as List<PlannerItem>)

            dataRetrievedSuccessfully(combinedData, PLANNER_DATA_ANIMALS)
            super.retrieveDataForDate(date)
        }
    }

    companion object {
        const val PLANNER_DATA_VACCINE_1 = "com.farmexpert.android.PlannerData.Vaccine1"
        const val PLANNER_DATA_VACCINE_2 = "com.farmexpert.android.PlannerData.Vaccine2"
        const val PLANNER_DATA_VACCINE_3 = "com.farmexpert.android.PlannerData.Vaccine3"
        const val PLANNER_DATA_VACCINE_4 = "com.farmexpert.android.PlannerData.Vaccine4"
    }
}
