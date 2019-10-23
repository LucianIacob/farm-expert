/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 10/23/19 12:26 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

import android.content.res.Resources
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.farmexpert.android.R

object SpinnerUtils {

    fun configureSpinner(
        spinner: Spinner,
        values: Array<String>,
        selected: Int? = null
    ) {
        ArrayAdapter(
            spinner.context,
            R.layout.spinner_item_header,
            values
        ).apply {
            setDropDownViewResource(R.layout.spinner_item_layout)
            spinner.adapter = this
        }

        selected?.let {
            spinner.setSelection(selected - 1, true)
        }
    }

    fun getBreedingNoteByPosition(
        selectedItem: Int,
        resources: Resources
    ) = resources.getIntArray(R.array.breeding_notes)[selectedItem]

    fun getBirthNoteByPosition(
        selectedItem: Int,
        resources: Resources
    ) = resources.getIntArray(R.array.birth_notes)[selectedItem]

    fun getGenderByPosition(
        selectedItemPosition: Int,
        resources: Resources
    ) = resources.getIntArray(R.array.gender_types)[selectedItemPosition]

    fun getBirthNoteValue(note: Int, resources: Resources): String =
        resources.getStringArray(R.array.birth_notes_values)[note - 1]

    fun getBreedingNoteValue(note: Int, resources: Resources): String =
        resources.getStringArray(R.array.breeding_notes_values)[note - 1]

    fun getGenderByKey(genderId: Int, resources: Resources): String =
        resources.getStringArray(R.array.gender_types_values)[genderId]
}