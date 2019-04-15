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

    private lateinit var mDateView: TextView

    protected var documentId: String? = null

    protected lateinit var mView: View
    protected lateinit var mActionDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            documentId = it.getString(EDIT_DIALOG_DOC_ID)
            mActionDate = Date(it.getLong(EDIT_DIALOG_DATE))
            extractAdditionalArgs()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = activity!!.layoutInflater.inflate(getLayoutId(), null)
        mDateView = mView.findViewById(R.id.actionDate)
        mDateView.text = mActionDate.getShort()
        mDateView.setOnClickListener { onChangeDateClick() }

        populateFields()
        return AlertDialog.Builder(activity!!)
            .setTitle(getTitle())
            .setView(mView)
            .setPositiveButton(R.string.dialog_update_btn) { _, _ -> sendNewRecord() }
            .setNegativeButton(R.string.dialog_cancel_btn, null)
            .create().also { it.setCanceledOnTouchOutside(false) }
    }

    private fun onChangeDateClick() {
        val calendar = Calendar.getInstance().apply { time = mActionDate }
        DatePickerDialog(
            activity!!,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                mActionDate = AppUtils.getTime(year, month, dayOfMonth)
                mDateView.text = mActionDate.getShort()
            },
            calendar.year(),
            calendar.month(),
            calendar.day()
        ).show()
    }

    abstract fun sendNewRecord()

    open fun extractAdditionalArgs() {}

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