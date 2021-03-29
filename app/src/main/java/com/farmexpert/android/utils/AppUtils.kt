/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 10/23/19 12:26 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

import android.content.Context
import android.widget.ImageView
import com.farmexpert.android.R
import com.farmexpert.android.activities.ConfigurationActivity.Companion.FARM_TIMELINE_PREFS
import com.google.firebase.Timestamp
import java.util.*

object AppUtils {

    const val NAIL_WITH_PROBLEM = "1"
    const val NAIL_WITHOUT_PROBLEM = "0"

    fun getTime(year: Int, month: Int, day: Int): Date =
        with(Calendar.getInstance()) {
            set(year, month, day)
            time
        }

    fun timestampFor(year: Int, month: Int, day: Int): Timestamp =
        with(Calendar.getInstance()) {
            set(year, month, day)
            Timestamp(time)
        }

    fun getExpectedBirthDate(breedingDate: Date, context: Context?): Date {
        val breedingCalendar = Calendar.getInstance().apply { time = breedingDate }

        val prefs = context?.getSharedPreferences(
            FARM_TIMELINE_PREFS,
            Context.MODE_PRIVATE
        )

        // todo check keys
        val key = context?.getString(R.string.pref_gestation_length_key)
        val gestationLength = prefs?.getInt(
            key,
            ConfigPickerUtils.getDefaultValue(
                context.getString(R.string.pref_gestation_length_key),
                context.resources
            )
        )

        gestationLength?.let { breedingCalendar.add(Calendar.DATE, it) }
        return breedingCalendar.time
    }

    fun populateLeftNail(imageView: ImageView?, char: String?) =
        if (char == NAIL_WITH_PROBLEM) {
            imageView?.setImageResource(R.drawable.left_nail_problem)
            R.drawable.left_nail_problem
        } else {
            imageView?.setImageResource(R.drawable.left_nail_default)
            R.drawable.left_nail_default
        }

    fun populateRightNail(imageView: ImageView?, char: String?) =
        if (char == NAIL_WITH_PROBLEM) {
            imageView?.setImageResource(R.drawable.right_nail_problem)
            R.drawable.right_nail_problem
        } else {
            imageView?.setImageResource(R.drawable.right_nail_default)
            R.drawable.right_nail_default
        }

    fun getStartOfTheYear(selectedYear: String): Timestamp =
        with(Calendar.getInstance()) {
            set(Calendar.YEAR, selectedYear.toInt())
            set(Calendar.MONTH, getMinimum(Calendar.MONTH))
            set(Calendar.DAY_OF_YEAR, getMinimum(Calendar.DAY_OF_YEAR))
            set(Calendar.HOUR_OF_DAY, getMinimum(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, getMinimum(Calendar.MINUTE))
            Timestamp(time)
        }

    fun getEndOfTheYear(selectedYear: String): Timestamp =
        with(Calendar.getInstance()) {
            set(Calendar.YEAR, selectedYear.toInt())
            set(Calendar.MONTH, getMaximum(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, getMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, getMaximum(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, getMaximum(Calendar.MINUTE))
            Timestamp(time)
        }
}
