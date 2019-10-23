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
import com.farmexpert.android.utils.SpinnerUtils.getBirthNoteByPosition
import kotlinx.android.synthetic.main.dialog_edit_birth.view.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 19 January, 2018.
 */

class EditBirthDialogFragment : BaseEditRecordDialogFragment() {

    private var mNoteToSelect: Int = 4

    override fun getTitle() = R.string.edit_birth_title

    override fun extractAdditionalArgs() {
        arguments?.let { mNoteToSelect = it.getInt(EDIT_DIALOG_NOTE, 4) }
    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_edit_birth
    }

    override fun populateFields() {
        SpinnerUtils.configureSpinner(
            spinner = mView.notesSpinner,
            values = resources.getStringArray(R.array.birth_notes),
            selected = mNoteToSelect
        )
    }

    override fun sendNewRecord() {
        val args = bundleOf(
            EDIT_DIALOG_DOC_ID to documentId,
            EDIT_DIALOG_DATE to mActionDate.time,
            EDIT_DIALOG_NOTE to getBirthNoteByPosition(
                mView.notesSpinner.selectedItemPosition,
                resources
            )
        )
        val intent = Intent()
        intent.putExtras(args)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

}
