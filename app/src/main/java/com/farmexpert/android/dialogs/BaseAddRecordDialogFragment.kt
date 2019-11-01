/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/10/19 9:18 AM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.farmexpert.android.utils.*
import java.util.*

abstract class BaseAddRecordDialogFragment : DialogFragment() {

    internal lateinit var mSetDate: Date
    protected lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSetDate = savedInstanceState?.getLong(ADD_DIALOG_DATE)
            ?.takeIfExists()?.let { Date(it) }
            ?: run {
                arguments?.getLong(ADD_DIALOG_DATE)?.takeIfExists()?.let {
                    Date(it)
                } ?: Date()
            }
    }

    internal fun setupDate() {
        getDateView().text = mSetDate.getShort()
    }

    abstract fun getDateView(): TextView

    internal fun onChangeDateClick() {
        context?.run {
            val calendar = Calendar.getInstance().apply { time = mSetDate }
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    mSetDate = AppUtils.getTime(year, month, dayOfMonth)
                    setupDate()
                },
                calendar.year(),
                calendar.month(),
                calendar.day()
            ).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(ADD_DIALOG_DATE, mSetDate.time)
    }

    companion object {
        const val ADD_DIALOG_CALF = "com.farmexpert.android.CalfID"
        const val ADD_DIALOG_NOTE = "com.farmexpert.android.Note"
        const val ADD_DIALOG_DATE = "com.farmexpert.android.Date"
        const val ADD_DIALOG_ANIMAL = "com.farmexpert.android.AnimalId"
        const val ADD_DIALOG_GENDER = "com.farmexpert.android.Gender"
        const val ADD_DIALOG_RACE = "com.farmexpert.android.Race"
        const val ADD_DIALOG_FATHER = "com.farmexpert.android.FatherId"
        const val ADD_DIALOG_MOTHER = "com.farmexpert.android.MotherId"
        const val ADD_DIALOG_DETAILS = "com.farmexpert.android.Details"
        const val ADD_DIALOG_MALE = "com.farmexpert.android.Male"
    }
}