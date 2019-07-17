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
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.farmexpert.android.R
import kotlinx.android.synthetic.main.dialog_add_planner_item.view.*

class AddPlannerItemDialogFragment : DialogFragment() {

    companion object {
        val TAG: String = AddPlannerItemDialogFragment::class.java.simpleName
        const val REMINDER_DIALOG_DETAILS = "com.farmexpert.android.Planner.ReminderItemDetails"
    }

    private lateinit var mView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = View.inflate(activity, R.layout.dialog_add_planner_item, null)
        return AlertDialog.Builder(activity!!)
            .setView(mView)
            .setTitle(R.string.planner_item_add_title)
            .setPositiveButton(R.string.dialog_add_positive_btn) { _, _ -> addPlannerItem() }
            .setNegativeButton(R.string.dialog_add_negative_btn, null)
            .create().also { it.setCanceledOnTouchOutside(false) }
    }

    private fun addPlannerItem() {
        val reminderDetails = mView.dialog_planner_details.text.toString()
        val bundle = bundleOf(REMINDER_DIALOG_DETAILS to reminderDetails)
        val intent = Intent().apply { putExtras(bundle) }
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

}
