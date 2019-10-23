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
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.farmexpert.android.R
import com.farmexpert.android.utils.SpinnerUtils
import com.farmexpert.android.utils.SpinnerUtils.getBreedingNoteByPosition
import kotlinx.android.synthetic.main.dialog_add_breeding.view.*

class AddBreedingDialogFragment : BaseAddRecordDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = View.inflate(activity, R.layout.dialog_add_breeding, null)
        mView.dialogDate.setOnClickListener { onChangeDateClick() }
        SpinnerUtils.configureSpinner(
            spinner = mView.breedingNotes,
            values = resources.getStringArray(R.array.breeding_notes_values)
        )

        setupDate()
        return AlertDialog.Builder(activity!!)
            .setView(mView)
            .setTitle(R.string.add_breeding_title)
            .setPositiveButton(R.string.dialog_add_positive_btn) { _, _ -> addRecord() }
            .setNegativeButton(R.string.dialog_cancel_btn, null)
            .create().also { it.setCanceledOnTouchOutside(false) }
    }

    private fun addRecord() {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putLong(ADD_DIALOG_DATE, mSetDate.time)
        bundle.putString(ADD_DIALOG_MALE, mView.maleInput.text.toString())
        bundle.putInt(
            ADD_DIALOG_NOTE,
            getBreedingNoteByPosition(mView.breedingNotes.selectedItemPosition, resources)
        )
        intent.putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    override fun getDateView(): TextView = mView.dialogDate
}
