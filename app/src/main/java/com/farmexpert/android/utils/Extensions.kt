/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/8/19 1:42 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
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

fun Calendar.year() = this.get(Calendar.YEAR)
fun Calendar.month() = this.get(Calendar.MONTH)
fun Calendar.day() = this.get(Calendar.DAY_OF_MONTH)

fun Animal.yearOfBirth(): Int = Calendar.getInstance().apply { time = dateOfBirth.toDate() }.year()
fun Animal.monthOfBirth() = Calendar.getInstance().apply { time = dateOfBirth.toDate() }.month()
fun Animal.dayOfBirth() = Calendar.getInstance().apply { time = dateOfBirth.toDate() }.day()