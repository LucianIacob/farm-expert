/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 7/17/19 10:02 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.fragments.dialog

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
import com.farmexpert.android.dialogs.BaseAddRecordDialogFragment
import com.farmexpert.android.utils.applyFarmexpertStyle
import kotlinx.android.synthetic.main.dialog_add_planner_item.view.*
import java.util.*

class AddPlannerItemDialogFragment : BaseAddRecordDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context?.let { context ->
            mView = View.inflate(activity, R.layout.dialog_add_planner_item, null)
            mView.dialogDate.setOnClickListener { onChangeDateClick() }

            setupDate()
            return AlertDialog.Builder(context, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                .setView(mView)
                .setTitle(R.string.planner_item_add_title)
                .setPositiveButton(R.string.dialog_add_positive_btn) { _, _ -> addPlannerItem() }
                .setNegativeButton(R.string.dialog_add_negative_btn, null)
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

    private fun addPlannerItem() {
        val bundle = bundleOf(
            REMINDER_DIALOG_DETAILS to mView.dialog_planner_details.text.toString(),
            ADD_DIALOG_DATE to mSetDate.time
        )
        val intent = Intent().apply { putExtras(bundle) }
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    companion object {
        val TAG: String = AddPlannerItemDialogFragment::class.java.simpleName
        const val REMINDER_DIALOG_DETAILS = "com.farmexpert.android.Planner.ReminderItemDetails"

        fun getInstance(date: Date?) =
            AddPlannerItemDialogFragment().apply {
                arguments = bundleOf(ADD_DIALOG_DATE to date?.time)
            }
    }
}
