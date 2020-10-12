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
import com.farmexpert.android.utils.DropdownUtils.getBirthNoteByPosition
import kotlinx.android.synthetic.main.dialog_edit_birth.view.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 19 January, 2018.
 */

class EditBirthDialogFragment : BaseEditRecordDialogFragment() {

    private var noteToSelect: Int = 4

    override var titleRes = R.string.edit_birth_title

    override val layoutRes = R.layout.dialog_edit_birth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteToSelect = savedInstanceState?.getInt(EDIT_DIALOG_NOTE, -1)
            ?.takeIf { it != -1 }
            ?: run { arguments?.getInt(EDIT_DIALOG_NOTE, noteToSelect) ?: noteToSelect }
    }

    override fun onUiElementsReady() {
        super.onUiElementsReady()

        fillDropdownComponent(
            textView = mView?.dialogNote,
            stringArray = R.array.birth_notes_values,
            selected = noteToSelect
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            EDIT_DIALOG_NOTE,
            getBirthNoteByPosition(mView?.dialogNote, resources)
        )
    }

    override fun sendNewRecord() {
        val args = bundleOf(
            EDIT_DIALOG_DOC_ID to documentId,
            DIALOG_DATE to currentDate.time,
            EDIT_DIALOG_NOTE to getBirthNoteByPosition(mView?.dialogNote, resources),
            DIALOG_DETAILS to mView?.dialogDetails?.text?.toString()
        )
        val intent = Intent().putExtras(args)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

}
