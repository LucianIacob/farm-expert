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
import android.os.Bundle
import androidx.core.os.bundleOf
import com.farmexpert.android.R
import kotlinx.android.synthetic.main.dialog_edit_animal_action.view.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 19 January, 2018.
 */

class EditAnimalActionDialogFragment : BaseEditRecordDialogFragment() {

    override var titleRes = R.string.edit_animal_action_title

    override val layoutRes = R.layout.dialog_edit_animal_action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        titleRes = arguments?.getInt(EDIT_DIALOG_TITLE) ?: titleRes
    }

    override fun sendNewRecord() {
        val bundle = bundleOf(
            EDIT_DIALOG_DOC_ID to documentId,
            DIALOG_DATE to currentDate.time,
            DIALOG_DETAILS to mView?.dialogDetails?.text.toString()
        )
        val intent = Intent()
        intent.putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    companion object {
        const val EDIT_DIALOG_TITLE = "com.farmexpert.android.DialogTitle"
    }
}
