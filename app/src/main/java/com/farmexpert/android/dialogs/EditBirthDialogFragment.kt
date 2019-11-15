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

    private var details: String? = null
    private var noteToSelect: Int = 4

    override fun getTitle() = R.string.edit_birth_title

    override fun extractAdditionalArgs(savedInstanceState: Bundle?, bundle: Bundle) {
        noteToSelect = savedInstanceState?.getInt(EDIT_DIALOG_NOTE, -1)
            ?.takeIf { it != -1 }?.let { it }
            ?: run { bundle.getInt(EDIT_DIALOG_NOTE, noteToSelect) }
        details = savedInstanceState?.getString(EDIT_DIALOG_DETAILS)?.let { it }
            ?: bundle.getString(EDIT_DIALOG_DETAILS)
    }

    override fun getLayoutId(): Int {
        return R.layout.dialog_edit_birth
    }

    override fun populateFields() {
        mView?.detailsBox?.setText(details)
        mView?.notesSpinner?.let {
            SpinnerUtils.configureSpinner(
                spinner = it,
                values = resources.getStringArray(R.array.birth_notes_values),
                selected = noteToSelect
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mView?.notesSpinner?.selectedItemPosition?.let {
            outState.putInt(EDIT_DIALOG_NOTE, getBirthNoteByPosition(it, resources))
        }
    }

    override fun sendNewRecord() {
        val args = bundleOf(
            EDIT_DIALOG_DOC_ID to documentId,
            EDIT_DIALOG_DATE to mActionDate?.time,
            EDIT_DIALOG_NOTE to mView?.notesSpinner?.selectedItemPosition?.let {
                getBirthNoteByPosition(it, resources)
            },
            EDIT_DIALOG_DETAILS to mView?.detailsBox?.text?.toString()
        )
        val intent = Intent().putExtras(args)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

}
