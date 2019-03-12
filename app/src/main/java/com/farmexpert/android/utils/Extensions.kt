/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/11/19 8:44 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

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
