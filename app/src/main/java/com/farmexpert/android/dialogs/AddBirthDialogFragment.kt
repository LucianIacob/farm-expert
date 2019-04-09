/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/9/19 9:25 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.farmexpert.android.R
import com.farmexpert.android.utils.AppUtils
import kotlinx.android.synthetic.main.dialog_add_birth.view.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 08 January, 2018.
 */

class AddBirthDialogFragment : BaseAddRecordDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = View.inflate(activity, R.layout.dialog_add_birth, null)
        mView.dialogDate.setOnClickListener { onChangeDateClick() }
        mView.dialogNote.adapter =
            AppUtils.getSpinnerAdapter(activity!!, resources.getStringArray(R.array.birth_notes))

        setupDate()
        return AlertDialog.Builder(activity!!)
            .setView(mView)
            .setTitle(R.string.add_birth_title)
            .setPositiveButton(R.string.dialog_add_positive_btn) { _, _ -> addRecord() }
            .setNegativeButton(R.string.dialog_add_negative_btn, null)
            .create().apply { setCanceledOnTouchOutside(false) }
    }

    override fun getDateView(): TextView = mView.dialogDate

    private fun addRecord() {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putLong(ADD_DIALOG_DATE, mSetDate.time)
        bundle.putString(ADD_DIALOG_CALF, mView.calfId.text.toString())
        bundle.putString(ADD_DIALOG_NOTE, mView.dialogNote.selectedItem.toString())
        intent.putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

}