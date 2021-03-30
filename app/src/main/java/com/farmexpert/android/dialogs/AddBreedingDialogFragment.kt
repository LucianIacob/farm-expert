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
import com.farmexpert.android.utils.DropdownUtils.getBreedingNote
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_add_breeding.view.*

class AddBreedingDialogFragment : BaseAddRecordDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = View.inflate(activity, R.layout.dialog_add_breeding, null)
        onUiElementsReady()

        fillDropdownComponent(
            textView = mView?.breedingNotes,
            stringArray = R.array.breeding_notes_values,
            selected = resources.getStringArray(R.array.breeding_notes_values).size - 1
        )

        context?.let { context ->
            return MaterialAlertDialogBuilder(context)
                .setView(mView)
                .setTitle(R.string.add_breeding_title)
                .setPositiveButton(R.string.dialog_add_positive_btn) { _, _ -> addRecord() }
                .setNegativeButton(R.string.dialog_cancel_btn, null)
                .setCancelable(false)
                .create()
        } ?: throw IllegalArgumentException()
    }

    private fun addRecord() {
        val bundle = bundleOf(
            DIALOG_DATE to currentDate.time,
            ADD_DIALOG_MALE to mView?.maleInput?.text?.toString(),
            ADD_DIALOG_NOTE to getBreedingNote(mView?.breedingNotes, resources),
            ADD_DIALOG_DETAILS to mView?.dialogDetails?.text?.toString()
        )
        val intent = Intent().putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }
}
