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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.trimmedLength
import androidx.fragment.app.Fragment
import com.farmexpert.android.BuildConfig
import com.farmexpert.android.R
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Lucian Iacob on March 11, 2019.
 */

fun Context.toast(@StringRes message: Int) = Toast
    .makeText(this, message, Toast.LENGTH_SHORT)
    .show()

fun Context.toast(message: String) = Toast
    .makeText(this, message, Toast.LENGTH_SHORT)
    .show()

fun Context.longToast(@StringRes message: Int) = Toast
    .makeText(this, message, Toast.LENGTH_LONG)
    .show()

fun Context.longToast(message: String) = Toast
    .makeText(this, message, Toast.LENGTH_LONG)
    .show()

fun Fragment.toast(@StringRes message: Int) {
    context?.let {
        Toast
            .makeText(it, message, Toast.LENGTH_SHORT)
            .show()
    }
}

fun Fragment.longToast(@StringRes message: Int) {
    context?.let {
        Toast
            .makeText(it, message, Toast.LENGTH_LONG)
            .show()
    }
}

fun Context.info(loggingMessage: () -> String) {
    if (BuildConfig.DEBUG) {
        Log.i(this::javaClass.name, loggingMessage())
    }
}

fun Fragment.info(loggingMessage: () -> String) {
    if (BuildConfig.DEBUG) {
        Log.i(this::javaClass.name, loggingMessage())
    }
}

fun View.snackbar(@StringRes message: Int) = Snackbar
    .make(this, message, Snackbar.LENGTH_SHORT)
    .show()

fun Any.error(error: Throwable?) {
    error?.let {
        if (BuildConfig.DEBUG) {
            Log.e(this::javaClass.name, "", error)
        } else {
            FirebaseCrashlytics.getInstance().recordException(error)
        }
    }
}

inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, Any?>) =
    IntentUtils.internalStartActivity(this, T::class.java, params)

inline fun <reified T : Activity> Fragment.startActivity(vararg params: Pair<String, Any?>) {
    IntentUtils.internalStartActivity(requireActivity(), T::class.java, params)
}

fun <TResult> Task<TResult>.addLoggableFailureListener(block: ((Exception) -> Unit)? = null): Task<TResult> {
    addOnFailureListener {
        error(it)
        block?.invoke(it)
    }
    return this
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun Timestamp.asDisplayable(onTwoLines: Boolean = false): String {
    val pattern = onTwoLines.takeIfTrue()?.let { "dd.MM\nyyyy" } ?: "dd.MM.yyyy"
    val format = SimpleDateFormat(pattern, Locale.getDefault())
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

fun Calendar.month() = this.get(Calendar.MONTH)
fun Calendar.day() = this.get(Calendar.DAY_OF_MONTH)

fun Date.month(): Int {
    return Calendar.getInstance().apply { time = this@month }.month()
}

fun Date.day(): Int {
    return Calendar.getInstance().apply { time = this@day }.day()
}

fun Button.applyFarmexpertStyle(context: Context, redButton: Boolean = false) {
    setBackgroundResource(0)
    setTextColor(
        ContextCompat.getColor(
            context,
            redButton.takeIf { it }?.let { R.color.red } ?: R.color.sea_green
        )
    )
}

fun Fragment.alert(
    message: Int,
    isCancellable: Boolean = true,
    okListener: (() -> Unit)? = null
) {
    activity?.alert(
        message = message,
        isCancellable = isCancellable,
        okListener = okListener
    )
}

fun Activity.alert(
    message: Any,
    isCancellable: Boolean = true,
    negativeButton: Boolean = false,
    okListener: (() -> Unit)? = null,
    redButton: Boolean = false
) {
    val stringMessage = (message as? Int)?.let { getString(it) }
        ?: message as? String
        ?: return

    val builder = MaterialAlertDialogBuilder(this)
        .setMessage(stringMessage)
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
            }
            show()
        }
}

fun String?.takeIfNotBlank(): String? = this.takeUnless { this.isNullOrBlank() }

fun Long.takeIfExists() = takeIf { it != 0L }

fun Boolean.takeIfTrue(): Boolean? = takeIf { it }

fun CharSequence?.isValidInput(): CharSequence? =
    this?.trimmedLength()?.takeIf { it > 0 }?.let { this }