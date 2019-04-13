/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/13/19 9:17 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.Activity
import android.content.Intent
import androidx.core.os.bundleOf
import com.farmexpert.android.R
import com.farmexpert.android.utils.AppUtils
import kotlinx.android.synthetic.main.dialog_edit_birth.view.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 19 January, 2018.
 */

class EditBirthDialogFragment : BaseEditRecordDialogFragment() {

    private lateinit var mNoteToSelect: String

    override fun getTitle() = R.string.edit_birth_title

    override fun extractAdditionalArgs() {
        mNoteToSelect =
            arguments!!.getString(EDIT_DIALOG_NOTE, getString(R.string.default_birth_note))
    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_edit_birth
    }

    override fun populateFields() {
        AppUtils.configureSpinner(
            spinner = mView.notesSpinner,
            elements = resources.getStringArray(R.array.birth_notes),
            selectedElement = mNoteToSelect
        )
    }

    override fun sendNewRecord() {
        val args = bundleOf(
            EDIT_DIALOG_DATE to mActionDate.time,
            EDIT_DIALOG_NOTE to mView.notesSpinner.selectedItem
        )
        val intent = Intent()
        intent.putExtras(args)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

}
