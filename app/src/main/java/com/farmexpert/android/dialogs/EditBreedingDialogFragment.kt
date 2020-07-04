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
import com.farmexpert.android.utils.SpinnerUtils
import kotlinx.android.synthetic.main.dialog_edit_breeding.view.*


/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 19 January, 2018.
 */

class EditBreedingDialogFragment : BaseEditRecordDialogFragment() {

    private var details: String? = null
    private var selectedNote: Int = 5
    private var breedingMale: String? = null

    override fun getTitle() = R.string.edit_breeding_title

    override fun extractAdditionalArgs(savedInstanceState: Bundle?, bundle: Bundle) {
        with(bundle) {
            selectedNote = savedInstanceState?.getInt(EDIT_DIALOG_NOTE, -1)
                ?.takeIf { it != -1 }
                ?: getInt(EDIT_DIALOG_NOTE, selectedNote)
            breedingMale = getString(EDIT_DIALOG_MALE)
            details = savedInstanceState?.getString(EDIT_DIALOG_DETAILS)
                ?: getString(EDIT_DIALOG_DETAILS)
        }
    }

    override fun populateFields() {
        mView?.maleInput?.setText(breedingMale)
        mView?.notesSpinner?.let {
            SpinnerUtils.configureSpinner(
                spinner = it,
                values = resources.getStringArray(R.array.breeding_notes_values),
                selected = selectedNote
            )
        }
        mView?.detailsBox?.setText(details)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EDIT_DIALOG_NOTE, selectedNote)
    }

    override fun getLayoutId() = R.layout.dialog_edit_breeding

    override fun sendNewRecord() {
        val bundle = bundleOf(
            EDIT_DIALOG_DOC_ID to documentId,
            EDIT_DIALOG_DATE to mActionDate?.time,
            EDIT_DIALOG_MALE to mView?.maleInput?.text.toString(),
            EDIT_DIALOG_NOTE to mView?.notesSpinner?.selectedItemPosition?.let {
                SpinnerUtils.getBreedingNoteByPosition(it, resources)
            },
            EDIT_DIALOG_DETAILS to mView?.detailsBox?.text?.toString()
        )
        val intent = Intent().putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }
}
