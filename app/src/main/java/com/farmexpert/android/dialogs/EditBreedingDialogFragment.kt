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
import android.os.Bundle
import androidx.core.os.bundleOf
import com.farmexpert.android.R
import com.farmexpert.android.utils.DropdownUtils
import kotlinx.android.synthetic.main.dialog_edit_breeding.view.*


/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 19 January, 2018.
 */

class EditBreedingDialogFragment : BaseEditRecordDialogFragment() {

    private var selectedNote: Int = 5
    private var breedingMale: String? = null

    override var titleRes = R.string.edit_breeding_title

    override val layoutRes = R.layout.dialog_edit_breeding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedNote = savedInstanceState?.getInt(EDIT_DIALOG_NOTE, -1)
            ?.takeIf { it != -1 }
            ?: arguments?.getInt(EDIT_DIALOG_NOTE, selectedNote) ?: selectedNote
        breedingMale = arguments?.getString(EDIT_DIALOG_MALE)
    }

    override fun onUiElementsReady() {
        super.onUiElementsReady()

        fillDropdownComponent(mView?.notesSpinner, R.array.breeding_notes_values, selectedNote - 1)
        mView?.maleInput?.setText(breedingMale)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EDIT_DIALOG_NOTE, selectedNote)
    }

    override fun sendNewRecord() {
        val bundle = bundleOf(
            EDIT_DIALOG_DOC_ID to documentId,
            DIALOG_DATE to currentDate.time,
            EDIT_DIALOG_MALE to mView?.maleInput?.text.toString(),
            EDIT_DIALOG_NOTE to DropdownUtils.getBreedingNote(mView?.notesSpinner, resources),
            DIALOG_DETAILS to mView?.dialogDetails?.text?.toString()
        )
        val intent = Intent().putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }
}
