/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/22/19 6:56 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.farmexpert.android.R
import com.farmexpert.android.utils.*
import kotlinx.android.synthetic.main.dialog_add_animal.view.*
import java.util.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 05 January, 2018.
 */

class AddAnimalDialogFragment : DialogFragment() {

    companion object {
        val TAG: String = AddAnimalDialogFragment::class.java.simpleName
        const val ADD_DIALOG_ID = "add_dialog_id"
        const val ADD_DIALOG_DATE = "add_dialog_date"
        const val ADD_DIALOG_GENRE = "add_dialog_genre"
        const val ADD_DIALOG_RACE = "add_dialog_race"
        const val ADD_DIALOG_FATHER = "add_dialog_father"
        const val ADD_DIALOG_MOTHER = "add_dialog_mother"
    }

    private lateinit var mSetDate: Date
    private lateinit var mView: View

    private val mDatePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        mSetDate = AppUtils.getTime(year, month, dayOfMonth)
        setupDate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSetDate = Date()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = View.inflate(activity, R.layout.dialog_add_animal, null)
        mView.dialogDate.setOnClickListener { onChangeDateClick() }
        mView.dialogGenreSpinner.adapter =
            AppUtils.getSpinnerAdapter(activity!!, resources.getStringArray(R.array.genre_types))

        setupDate()
        return AlertDialog.Builder(activity!!)
            .setView(mView)
            .setTitle(R.string.add_animal_title)
            .setPositiveButton(R.string.dialog_add_positive_btn) { _, _ -> addAnimal() }
            .setNegativeButton(R.string.dialog_add_negative_btn, null)
            .create().also { it.setCanceledOnTouchOutside(false) }
    }

    private fun setupDate() {
        mView.dialogDate.text = mSetDate.getShort()
    }

    private fun onChangeDateClick() {
        val c = Calendar.getInstance().also { it.time = mSetDate }
        DatePickerDialog(activity!!, mDatePickerListener, c.year(), c.month(), c.day()).show()
    }

    private fun addAnimal() {
        val matricol = mView.dialogId.text.toString()
        val intent = Intent()
        val bundle = Bundle()
        bundle.putString(ADD_DIALOG_ID, matricol)
        bundle.putLong(ADD_DIALOG_DATE, mSetDate.time)
        bundle.putString(ADD_DIALOG_GENRE, mView.dialogGenreSpinner.selectedItem.toString())
        bundle.putString(ADD_DIALOG_RACE, mView.dialogRace.text.toString())
        bundle.putString(ADD_DIALOG_FATHER, mView.dialogFather.text.toString())
        bundle.putString(ADD_DIALOG_MOTHER, mView.dialogMother.text.toString())
        intent.putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

}
