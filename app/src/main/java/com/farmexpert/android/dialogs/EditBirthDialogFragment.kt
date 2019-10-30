/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 10/23/19 1:57 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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

    private var noteToSelect: Int = 4

    override fun getTitle() = R.string.edit_birth_title

    override fun extractAdditionalArgs(bundle: Bundle) {
        noteToSelect = bundle.getInt(EDIT_DIALOG_NOTE, noteToSelect)
    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_edit_birth
    }

    override fun populateFields() {
        mView?.notesSpinner?.let {
            SpinnerUtils.configureSpinner(
                spinner = it,
                values = resources.getStringArray(R.array.birth_notes_values),
                selected = noteToSelect
            )
        }
    }

    override fun sendNewRecord() {
        val args = bundleOf(
            EDIT_DIALOG_DOC_ID to documentId,
            EDIT_DIALOG_DATE to mActionDate?.time,
            EDIT_DIALOG_NOTE to mView?.notesSpinner?.selectedItemPosition?.let {
                getBirthNoteByPosition(it, resources)
            }
        )
        val intent = Intent().putExtras(args)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

}
