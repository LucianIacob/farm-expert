/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/13/19 9:05 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.farmexpert.android.R
import kotlinx.android.synthetic.main.dialog_add_animal_action.view.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 08 January, 2018.
 */

class AddAnimalActionDialogFragment : BaseAddRecordDialogFragment() {

    @StringRes
    var titleId: Int = R.string.empty_text

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titleId = arguments?.getInt(ADD_DIALOG_TITLE, R.string.empty_text)!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = View.inflate(activity!!, R.layout.dialog_add_animal_action, null)
        mView.dialogDate.setOnClickListener { onChangeDateClick() }
        setupDate()
        return AlertDialog.Builder(activity!!)
            .setView(mView)
            .setTitle(titleId)
            .setPositiveButton(R.string.dialog_add_positive_btn) { _, _ -> addRecord() }
            .setNegativeButton(R.string.dialog_cancel_btn, null)
            .create().also { it.setCanceledOnTouchOutside(false) }
    }

    override fun getDateView(): TextView = mView.dialogDate

    private fun addRecord() {
        val bundle = Bundle()
        bundle.putString(ADD_DIALOG_DETAILS, mView.dialogDetails.text.toString())
        bundle.putLong(ADD_DIALOG_DATE, mSetDate.time)
        val intent = Intent().apply { putExtras(bundle) }
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    companion object {
        const val ADD_DIALOG_TITLE = "com.farmexpert.android.DialogTitle"
    }
}
