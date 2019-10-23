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
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.farmexpert.android.R
import com.farmexpert.android.utils.SpinnerUtils
import com.farmexpert.android.utils.SpinnerUtils.getGenderByPosition
import kotlinx.android.synthetic.main.dialog_add_animal.view.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 05 January, 2018.
 */

class AddAnimalDialogFragment : BaseAddRecordDialogFragment() {

    companion object {
        val TAG: String = AddAnimalDialogFragment::class.java.simpleName
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = View.inflate(activity, R.layout.dialog_add_animal, null)
        mView.dialogDate.setOnClickListener { onChangeDateClick() }
        SpinnerUtils.configureSpinner(
            spinner = mView.dialogGenderSpinner,
            values = resources.getStringArray(R.array.gender_types_values),
            selected = 1
        )

        setupDate()
        return AlertDialog.Builder(activity!!)
            .setView(mView)
            .setTitle(R.string.add_animal_title)
            .setPositiveButton(R.string.dialog_add_positive_btn) { _, _ -> addAnimal() }
            .setNegativeButton(R.string.dialog_cancel_btn, null)
            .create().also { it.setCanceledOnTouchOutside(false) }
    }

    override fun getDateView(): TextView = mView.dialogDate

    private fun addAnimal() {
        val id = mView.dialogId.text.toString()
        val intent = Intent()
        val bundle = Bundle()
        bundle.putString(ADD_DIALOG_ANIMAL, id)
        bundle.putLong(ADD_DIALOG_DATE, mSetDate.time)
        bundle.putInt(
            ADD_DIALOG_GENDER,
            getGenderByPosition(mView.dialogGenderSpinner.selectedItemPosition, resources)
        )
        bundle.putString(ADD_DIALOG_RACE, mView.dialogRace.text.toString())
        bundle.putString(ADD_DIALOG_FATHER, mView.dialogFather.text.toString())
        bundle.putString(ADD_DIALOG_MOTHER, mView.dialogMother.text.toString())
        intent.putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

}
