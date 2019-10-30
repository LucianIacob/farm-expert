/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 10/23/19 12:26 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import com.farmexpert.android.R
import com.farmexpert.android.utils.SpinnerUtils
import com.farmexpert.android.utils.SpinnerUtils.getBirthNoteByPosition
import com.farmexpert.android.utils.applyFarmexpertStyle
import kotlinx.android.synthetic.main.dialog_add_birth.view.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 08 January, 2018.
 */

class AddBirthDialogFragment : BaseAddRecordDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = View.inflate(activity, R.layout.dialog_add_birth, null)
        mView.dialogDate.setOnClickListener { onChangeDateClick() }
        SpinnerUtils.configureSpinner(
            spinner = mView.dialogNote,
            values = resources.getStringArray(R.array.birth_notes_values)
        )

        setupDate()
        context?.run {
            return AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                .setView(mView)
                .setTitle(R.string.add_birth_title)
                .setPositiveButton(R.string.dialog_add_positive_btn) { _, _ -> addRecord() }
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

    override fun getDateView(): TextView = mView.dialogDate

    private fun addRecord() {
        val bundle = bundleOf(
            ADD_DIALOG_DATE to mSetDate.time,
            ADD_DIALOG_CALF to mView.calfId.text.toString(),
            ADD_DIALOG_NOTE to getBirthNoteByPosition(
                mView.dialogNote.selectedItemPosition,
                resources
            )
        )
        val intent = Intent().putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

}