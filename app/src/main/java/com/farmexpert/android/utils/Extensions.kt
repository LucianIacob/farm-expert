/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 10/21/19 5:25 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.farmexpert.android.R
import com.farmexpert.android.model.Animal
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Lucian Iacob on March 11, 2019.
 */

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun Timestamp.asDisplayable(): String {
    val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return format.format(this.toDate())
}

fun Timestamp.month(): Int {
    val date = toDate()
    return date.month()
}

fun Timestamp.day(): Int {
    val date = toDate()
    return date.day()
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hidden() {
    visibility = View.GONE
}

fun Date.getShort(): String {
    val format = "dd.MM.yyyy"
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    return simpleDateFormat.format(this)
}

fun Date.getPrevDay(): Date {
    val calendar = Calendar.getInstance().also { it.time = this }
    calendar.add(Calendar.DATE, -1)
    return calendar.time
}

fun Date.getNextDay(): Date {
    val calendar = Calendar.getInstance().also { it.time = this }
    calendar.add(Calendar.DATE, 1)
    return calendar.time
}

fun Date.shift(days: Int = 0, jumpTo: TimeOfTheDay = TimeOfTheDay.DEFAULT): Date {
    val calendar = Calendar.getInstance().also { it.time = this }
    calendar.add(Calendar.DATE, days)

    when (jumpTo) {
        TimeOfTheDay.START -> {
            calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY))
            calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE))
            calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND))
        }
        TimeOfTheDay.END -> {
            calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY))
            calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE))
            calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND))
        }
        TimeOfTheDay.DEFAULT -> {
        }
    }

    return calendar.time
}

fun Calendar.year() = this.get(Calendar.YEAR)
fun Calendar.month() = this.get(Calendar.MONTH)
fun Calendar.day() = this.get(Calendar.DAY_OF_MONTH)

fun Animal.yearOfBirth(): Int = Calendar.getInstance().apply { time = dateOfBirth.toDate() }.year()
fun Animal.monthOfBirth() = dateOfBirth.toDate().month()
fun Animal.dayOfBirth() = dateOfBirth.toDate().day()

fun Date.month(): Int {
    return Calendar.getInstance().apply { time = this@month }.month()
}

fun Date.day(): Int {
    return Calendar.getInstance().apply { time = this@day }.day()
}

fun Button.applyFarmexpertStyle(context: Context) {
    setBackgroundResource(0)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setTextColor(resources.getColor(R.color.sea_green, null))
    } else {
        setTextColor(ContextCompat.getColor(context, R.color.sea_green))
    }
}