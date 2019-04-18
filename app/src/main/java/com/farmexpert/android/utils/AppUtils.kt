/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/18/19 9:35 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import com.farmexpert.android.R
import com.farmexpert.android.activities.ConfigurationActivity.Companion.FARM_TIMELINE_PREFS
import com.farmexpert.android.app.FarmExpertApplication
import com.google.firebase.Timestamp
import java.util.*

class AppUtils {

    companion object {

        private const val NAIL_WITH_PROBLEM = "1"
        private const val NAIL_WITHOUT_PROBLEM = "0"

        fun getTime(year: Int, month: Int, day: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            return calendar.time
        }

        fun configureSpinner(
            spinner: Spinner,
            elements: Array<out String>,
            selectedElement: String? = null
        ) {
            val spinnerAdapter =
                ArrayAdapter(spinner.context, R.layout.spinner_item_header, elements).apply {
                    setDropDownViewResource(R.layout.spinner_item_layout)
                }
            spinner.adapter = spinnerAdapter

            val selectedElementIndex = elements.indexOf(selectedElement)
            spinner.setSelection(
                if (selectedElementIndex == -1) elements.size - 1 else selectedElementIndex,
                true
            )
        }

        fun timestampFor(year: Int, month: Int, day: Int): Timestamp {
            val calendar = Calendar.getInstance().apply { set(year, month, day) }
            return Timestamp(calendar.time)
        }

        fun getExpectedBirth(breedingDate: Date): Date? {
            val breedingCalendar = Calendar.getInstance().apply { time = breedingDate }

            val resources = FarmExpertApplication.appContext.resources
            val prefs = FarmExpertApplication.appContext.getSharedPreferences(
                FARM_TIMELINE_PREFS,
                Context.MODE_PRIVATE
            )

            // todo check keys
            val key = resources.getString(R.string.pref_gestation_length_key)
            val gestationLength = prefs.getInt(
                key,
                ConfigPickerUtils.getDefaultValue(
                    resources.getString(R.string.pref_gestation_length_key),
                    resources
                )
            )

            breedingCalendar.add(Calendar.DATE, gestationLength)
            return breedingCalendar.time
        }

        fun getByteByNail(drawableId: Int?): Any {
            return if (drawableId == R.drawable.left_nail_problem || drawableId == R.drawable.right_nail_problem) {
                NAIL_WITH_PROBLEM
            } else {
                NAIL_WITHOUT_PROBLEM
            }
        }

        fun populateLeftNail(imageView: ImageView?, char: String): Int {
            return if (char == NAIL_WITH_PROBLEM) {
                imageView?.setImageResource(R.drawable.left_nail_problem)
                R.drawable.left_nail_problem
            } else {
                imageView?.setImageResource(R.drawable.left_nail_default)
                R.drawable.left_nail_default
            }
        }

        fun populateRightNail(imageView: ImageView?, char: String): Int {
            return if (char == NAIL_WITH_PROBLEM) {
                imageView?.setImageResource(R.drawable.right_nail_problem)
                R.drawable.right_nail_problem
            } else {
                imageView?.setImageResource(R.drawable.right_nail_default)
                R.drawable.right_nail_default
            }
        }

        fun getStartOfTheYear(selectedYear: String): Timestamp {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, selectedYear.toInt())
            calendar.set(Calendar.MONTH, Calendar.JANUARY)
            calendar.set(Calendar.DAY_OF_YEAR, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)

            return Timestamp(calendar.time)
        }

        fun getEndOfTheYear(selectedYear: String): Timestamp {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, selectedYear.toInt())
            calendar.set(Calendar.MONTH, Calendar.DECEMBER)
            calendar.set(Calendar.DAY_OF_MONTH, 31)
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)

            return Timestamp(calendar.time)
        }
    }
}
