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
import android.content.Intent
import androidx.core.os.bundleOf
import com.farmexpert.android.R
import com.farmexpert.android.utils.SpinnerUtils
import kotlinx.android.synthetic.main.dialog_edit_breeding.view.*


/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 19 January, 2018.
 */

class EditBreedingDialogFragment : BaseEditRecordDialogFragment() {

    private var selectedNote: Int = 5
    private lateinit var breedingMale: String

    override fun getTitle() = R.string.edit_breeding_title

    override fun extractAdditionalArgs() {
        selectedNote = arguments!!.getInt(EDIT_DIALOG_NOTE, 5)
        breedingMale = arguments!!.getString(EDIT_DIALOG_MALE, "")
    }

    override fun populateFields() {
        mView.maleInput.setText(breedingMale)
        SpinnerUtils.configureSpinner(
            spinner = mView.notesSpinner,
            values = resources.getStringArray(R.array.breeding_notes_values),
            selected = selectedNote
        )
    }

    override fun getLayoutId() = R.layout.dialog_edit_breeding

    override fun sendNewRecord() {
        val bundle = bundleOf(
            EDIT_DIALOG_DOC_ID to documentId,
            EDIT_DIALOG_DATE to mActionDate.time,
            EDIT_DIALOG_MALE to mView.maleInput.text.toString(),
            EDIT_DIALOG_NOTE to SpinnerUtils.getBreedingNoteByPosition(
                mView.notesSpinner.selectedItemPosition,
                resources
            )
        )
        val intent = Intent()
        intent.putExtras(bundle)
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }
}
