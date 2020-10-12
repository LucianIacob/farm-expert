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
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.farmexpert.android.R
import com.farmexpert.android.utils.DropdownUtils.getBirthNoteByPosition
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_add_birth.view.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 08 January, 2018.
 */

class AddBirthDialogFragment : BaseAddRecordDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = View.inflate(activity, R.layout.dialog_add_birth, null)
        onUiElementsReady()
        fillDropdownComponent(mView?.dialogNote, R.array.birth_notes_values, 3)

        context?.run {
            return MaterialAlertDialogBuilder(this)
                .setView(mView)
                .setTitle(R.string.add_birth_title)
                .setPositiveButton(R.string.dialog_add_positive_btn) { _, _ -> addRecord() }
                .setNegativeButton(R.string.dialog_cancel_btn, null)
                .setCancelable(false)
                .create()
        } ?: throw IllegalArgumentException()
    }

    private fun addRecord() {
        val bundle = bundleOf(
            DIALOG_DATE to currentDate.time,
            ADD_DIALOG_CALF to mView?.calfId?.text?.toString(),
            ADD_DIALOG_NOTE to getBirthNoteByPosition(mView?.dialogNote, resources),
            ADD_DIALOG_DETAILS to mView?.dialogDetails?.text?.toString()
        )
        val intent = Intent().putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

}