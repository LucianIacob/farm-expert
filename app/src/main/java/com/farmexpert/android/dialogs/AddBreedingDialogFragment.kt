/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/10/19 9:18 AM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.farmexpert.android.R
import com.farmexpert.android.utils.AppUtils
import kotlinx.android.synthetic.main.dialog_add_breeding.view.*

class AddBreedingDialogFragment : BaseAddRecordDialogFragment(), TextWatcher {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = View.inflate(activity, R.layout.dialog_add_breeding, null)
        mView.dialogDate.setOnClickListener { onChangeDateClick() }
        mView.maleInput.addTextChangedListener(this)
        mView.breedingNotes.adapter =
            AppUtils.getSpinnerAdapter(activity!!, resources.getStringArray(R.array.breeding_notes))

        setupDate()
        return AlertDialog.Builder(activity!!)
            .setView(mView)
            .setTitle(R.string.add_breeding_title)
            .setPositiveButton(R.string.dialog_add_positive_btn) { _, _ -> addRecord() }
            .setNegativeButton(R.string.dialog_add_negative_btn, null)
            .create().also { it.setCanceledOnTouchOutside(false) }
    }

    private fun addRecord() {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putLong(ADD_DIALOG_DATE, mSetDate.time)
        bundle.putString(ADD_DIALOG_MALE, mView.maleInput.text.toString())
        bundle.putString(ADD_DIALOG_NOTE, mView.breedingNotes.selectedItem.toString())
        intent.putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    override fun getDateView(): TextView = mView.dialogDate

    // todo check whether this should be used anymore
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {

    }
}
