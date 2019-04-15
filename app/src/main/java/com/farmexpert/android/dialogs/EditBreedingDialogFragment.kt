/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/15/19 1:08 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.Activity
import android.content.Intent
import androidx.core.os.bundleOf
import com.farmexpert.android.R
import com.farmexpert.android.utils.AppUtils
import kotlinx.android.synthetic.main.dialog_edit_breeding.view.*


/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 19 January, 2018.
 */

class EditBreedingDialogFragment : BaseEditRecordDialogFragment() {

    private lateinit var selectedNote: String
    private lateinit var breedingMale: String

    override fun getTitle() = R.string.edit_breeding_title

    override fun extractAdditionalArgs() {
        selectedNote =
            arguments!!.getString(EDIT_DIALOG_NOTE, getString(R.string.default_breeding_note))
        breedingMale = arguments!!.getString(EDIT_DIALOG_MALE, "")
    }

    override fun populateFields() {
        mView.maleInput.setText(breedingMale)
        AppUtils.configureSpinner(
            spinner = mView.notesSpinner,
            elements = resources.getStringArray(R.array.breeding_notes),
            selectedElement = selectedNote
        )
    }

    override fun getLayoutId() = R.layout.dialog_edit_breeding

    override fun sendNewRecord() {
        val bundle = bundleOf(
            EDIT_DIALOG_DOC_ID to documentId,
            EDIT_DIALOG_DATE to mActionDate.time,
            EDIT_DIALOG_MALE to mView.maleInput.text.toString(),
            EDIT_DIALOG_NOTE to mView.notesSpinner.selectedItem
        )
        val intent = Intent()
        intent.putExtras(bundle)
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }
}
