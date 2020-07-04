/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 10/21/19 5:25 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.trimmedLength
import androidx.fragment.app.Fragment
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

fun View.gone() {
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

fun Button.applyFarmexpertStyle(context: Context, redButton: Boolean = false) {
    setBackgroundResource(0)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setTextColor(resources.getColor(
            redButton.takeIf { it }?.let {
                R.color.red
            } ?: R.color.sea_green,
            null)
        )
    } else {
        setTextColor(ContextCompat.getColor(
            context,
            redButton.takeIf { it }?.let {
                R.color.red
            } ?: R.color.sea_green)
        )
    }
}

fun Fragment.alert(
    message: Int,
    isCancellable: Boolean = true,
    okListener: (() -> Unit)? = null
) {
    context?.let { context ->
        AlertDialog.Builder(context, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                okListener?.invoke()
            }
            .setCancelable(isCancellable)
            .create()
            .run {
                setOnShowListener {
                    getButton(DialogInterface.BUTTON_POSITIVE)?.applyFarmexpertStyle(context)
                }
                show()
            }
    }
}

fun Activity.alert(
    message: Int,
    isCancellable: Boolean = true,
    negativeButton: Boolean = false,
    okListener: (() -> Unit)? = null,
    redButton: Boolean = false
) {
    val builder = AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok) { _, _ ->
            okListener?.invoke()
        }

    if (negativeButton) {
        builder.setNegativeButton(android.R.string.cancel, null)
    }

    builder
        .setCancelable(isCancellable)
        .create()
        .run {
            setOnShowListener {
                getButton(DialogInterface.BUTTON_POSITIVE)?.applyFarmexpertStyle(context, redButton)
                getButton(DialogInterface.BUTTON_NEGATIVE)?.applyFarmexpertStyle(context)
            }
            show()
        }
}

fun Activity.alert(
    message: String,
    isCancellable: Boolean = true,
    okListener: (() -> Unit)? = null
) {
    AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok) { _, _ ->
            okListener?.invoke()
        }
        .setCancelable(isCancellable)
        .create()
        .run {
            setOnShowListener {
                getButton(DialogInterface.BUTTON_POSITIVE)?.applyFarmexpertStyle(context)
            }
            show()
        }
}

fun Int.encode(): String =
    if (this == R.drawable.left_nail_problem || this == R.drawable.right_nail_problem) {
        AppUtils.NAIL_WITH_PROBLEM
    } else {
        AppUtils.NAIL_WITHOUT_PROBLEM
    }

fun String?.takeIfNotBlank(): String? = this.takeUnless { this.isNullOrBlank() }

fun Long.takeIfExists() = this.takeIf { it != 0L }

fun Boolean.takeIfTrue(): Boolean? = this.takeIf { it }

fun CharSequence?.isValidInput(): CharSequence? =
    this?.trimmedLength()?.takeIf { it > 0 }?.let { this }