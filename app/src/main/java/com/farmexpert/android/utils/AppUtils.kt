/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/22/19 6:41 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import com.farmexpert.android.R
import java.util.*

class AppUtils {

    companion object {

        fun getTime(year: Int, month: Int, day: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            return calendar.time
        }

        fun getSpinnerAdapter(context: Context, stringArray: Array<out String>?): SpinnerAdapter? {
            val spinnerAdapter = ArrayAdapter(context, R.layout.spinner_item_header, stringArray!!)
            spinnerAdapter.setDropDownViewResource(R.layout.spinner_item_layout)
            return spinnerAdapter
        }
    }
}
