/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/15/19 12:54 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

import android.view.View
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Lucian Iacob on March 11, 2019.
 */
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