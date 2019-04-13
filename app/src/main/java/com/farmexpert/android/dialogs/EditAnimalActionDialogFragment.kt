/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/13/19 11:14 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.Activity
import android.content.Intent
import androidx.core.os.bundleOf
import com.farmexpert.android.R
import kotlinx.android.synthetic.main.dialog_edit_animal_action.view.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 19 January, 2018.
 */

open class EditAnimalActionDialogFragment : BaseEditRecordDialogFragment() {

    private lateinit var mDefaultDetails: String
    private var mTitleRedId: Int = R.string.edit_animal_action_title

    override fun extractAdditionalArgs() {
        mDefaultDetails = arguments!!.getString(EDIT_DIALOG_DETAILS, "")
        mTitleRedId = arguments!!.getInt(EDIT_DIALOG_TITLE, R.string.edit_animal_action_title)
    }

    override fun populateFields() {
        mView.details.setText(mDefaultDetails)
    }

    override fun getLayoutId() = R.layout.dialog_edit_animal_action

    override fun getTitle() = mTitleRedId

    override fun sendNewRecord() {
        val bundle = bundleOf(
            EDIT_DIALOG_DATE to mActionDate.time,
            EDIT_DIALOG_DETAILS to mView.details.toString()
        )
        val intent = Intent()
        intent.putExtras(bundle)
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    companion object {
        const val EDIT_DIALOG_TITLE = "com.farmexpert.android.DialogTitle"
    }
}
