/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 8/4/19 10:12 PM.
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

class PlannerVaccinationsFragment : BasePlannerFragment() {

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

    override fun getPlannerContainer() = PlannerContainer.VACCINATIONS

    override fun getHeaderText(): String {
        return getString(R.string.planner_vaccinations_title)
    }

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
            .addOnSuccessListener {
                vaccine1Arrived = true
                val vaccine1 = PlannerDataTransformer
                    .transformVaccine1(it, daysOfFirstVaccine, resources)
                dataRetrieved(vaccine1, PLANNER_DATA_VACCINE_1, date)
            }
            .addOnFailureListener {
                error { it }
                Crashlytics.logException(it)
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
            .addOnSuccessListener {
                vaccine2Arrived = true
                val vaccine2 = PlannerDataTransformer
                    .transformBeforeBirthItems(it, daysOfSecondVaccine, resources)
                dataRetrieved(vaccine2, PLANNER_DATA_VACCINE_2, date)
            }
            .addOnFailureListener {
                error { it }
                Crashlytics.logException(it)
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
            .addOnSuccessListener {
                vaccine3Arrived = true
                val vaccine3 = PlannerDataTransformer
                    .transformBeforeBirthItems(it, daysOfThirdVaccine, resources)
                dataRetrieved(vaccine3, PLANNER_DATA_VACCINE_3, date)
            }
            .addOnFailureListener {
                error { it }
                Crashlytics.logException(it)
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
            .addOnSuccessListener {
                vaccine4Arrived = true
                val vaccine4 = PlannerDataTransformer
                    .transformBeforeBirthItems(it, daysOfForthVaccine, resources)
                dataRetrieved(vaccine4, PLANNER_DATA_VACCINE_4, date)
            }
            .addOnFailureListener {
                error { it }
                Crashlytics.logException(it)
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
