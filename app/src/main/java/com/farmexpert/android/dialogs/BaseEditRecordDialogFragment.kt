/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/15/19 1:08 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.farmexpert.android.R
import com.farmexpert.android.utils.*
import java.util.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, February 21, 2018.
 */
abstract class BaseEditRecordDialogFragment : DialogFragment() {

    private var mDateView: TextView? = null
    protected var documentId: String? = null
    protected var mView: View? = null
    protected var mActionDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            documentId = getString(EDIT_DIALOG_DOC_ID)
            extractAdditionalArgs(savedInstanceState, this)
        }

        mActionDate = savedInstanceState?.getLong(EDIT_DIALOG_DATE)
            ?.takeIfExists()?.let { Date(it) }
            ?: run {
                arguments?.getLong(EDIT_DIALOG_DATE)?.takeIfExists()?.let {
                    Date(it)
                } ?: Date()
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mActionDate?.time?.let { outState.putLong(EDIT_DIALOG_DATE, it) }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = activity?.layoutInflater?.inflate(getLayoutId(), null)
        mDateView = mView?.findViewById(R.id.actionDate)
        mDateView?.text = mActionDate?.getShort()
        mDateView?.setOnClickListener { onChangeDateClick() }

        populateFields()
        context?.run {
            return AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                .setTitle(getTitle())
                .setView(mView)
                .setPositiveButton(R.string.dialog_update_btn) { _, _ -> sendNewRecord() }
                .setNegativeButton(R.string.dialog_cancel_btn, null)
                .setCancelable(false)
                .create()
                .apply {
                    setOnShowListener {
                        getButton(BUTTON_NEGATIVE).applyFarmexpertStyle(context)
                        getButton(BUTTON_POSITIVE).applyFarmexpertStyle(context)
                    }
                }
        } ?: throw IllegalArgumentException()
    }

    private fun onChangeDateClick() {
        context?.run {
            val calendar = Calendar.getInstance().apply {
                mActionDate?.let { time = it }
            }
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    mActionDate = AppUtils.getTime(year, month, dayOfMonth)
                    mDateView?.text = mActionDate?.getShort()
                },
                calendar.year(),
                calendar.month(),
                calendar.day()
            ).show()
        }
    }

    abstract fun sendNewRecord()

    open fun extractAdditionalArgs(savedInstanceState: Bundle?, bundle: Bundle) {}

    abstract fun getLayoutId(): Int

    abstract fun getTitle(): Int

    abstract fun populateFields()

    companion object {
        const val EDIT_DIALOG_DOC_ID = "com.farmexpert.android.DocumentId"
        const val EDIT_DIALOG_NOTE = "com.farmexpert.android.Note"
        const val EDIT_DIALOG_DATE = "com.farmexpert.android.Date"
        const val EDIT_DIALOG_MALE = "com.farmexpert.android.Male"
        const val EDIT_DIALOG_DETAILS = "com.farmexpert.android.Details"
    }
}