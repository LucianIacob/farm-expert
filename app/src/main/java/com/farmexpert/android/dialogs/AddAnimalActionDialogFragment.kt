/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/13/19 9:05 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.farmexpert.android.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_add_animal_action.view.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 08 January, 2018.
 */

class AddAnimalActionDialogFragment : BaseAddRecordDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context?.let { context ->
            val title = arguments?.getInt(ADD_DIALOG_TITLE)?.takeIf { it != 0 }
                ?: R.string.empty_text

            mView = View.inflate(context, R.layout.dialog_add_animal_action, null)
            onUiElementsReady()
            return MaterialAlertDialogBuilder(context)
                .setView(mView)
                .setTitle(title)
                .setPositiveButton(R.string.dialog_add_positive_btn) { _, _ -> addRecord() }
                .setNegativeButton(R.string.dialog_cancel_btn, null)
                .setCancelable(false)
                .create()
        } ?: throw IllegalArgumentException()
    }

    private fun addRecord() {
        val bundle = bundleOf(
            ADD_DIALOG_DETAILS to mView?.dialogDetails?.text.toString(),
            DIALOG_DATE to currentDate.time
        )
        val intent = Intent().apply { putExtras(bundle) }
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    companion object {
        const val ADD_DIALOG_TITLE = "com.farmexpert.android.DialogTitle"
    }
}
