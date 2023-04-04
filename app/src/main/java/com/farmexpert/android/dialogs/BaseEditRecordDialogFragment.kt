/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.LayoutRes
import com.farmexpert.android.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, February 21, 2018.
 */
abstract class BaseEditRecordDialogFragment(@LayoutRes private val layoutRes: Int) :
    BaseDialogFragment() {

    abstract var titleRes: Int

    protected var documentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        documentId = arguments?.getString(EDIT_DIALOG_DOC_ID)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = activity?.layoutInflater?.inflate(layoutRes, null)
        onUiElementsReady()

        context?.run {
            return MaterialAlertDialogBuilder(this)
                .setTitle(titleRes)
                .setView(mView)
                .setPositiveButton(R.string.dialog_update_btn) { _, _ -> sendNewRecord() }
                .setNegativeButton(R.string.dialog_cancel_btn, null)
                .setCancelable(false)
                .create()
        } ?: throw IllegalArgumentException()
    }

    abstract fun sendNewRecord()

    companion object {
        const val EDIT_DIALOG_DOC_ID = "com.farmexpert.android.DocumentId"
        const val EDIT_DIALOG_NOTE = "com.farmexpert.android.Note"
        const val EDIT_DIALOG_MALE = "com.farmexpert.android.Male"
    }
}