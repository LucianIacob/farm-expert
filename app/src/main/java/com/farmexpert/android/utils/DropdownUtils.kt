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
import com.farmexpert.android.R
import com.google.android.material.textfield.MaterialAutoCompleteTextView

object DropdownUtils {

    fun getBreedingNote(
        textView: MaterialAutoCompleteTextView?,
        resources: Resources
    ): Int {
        val breedings = resources.getStringArray(R.array.breeding_notes_values)
        return breedings.indexOf(textView?.text.toString()).takeIf { it != -1 }
            ?: breedings.size - 1
    }

    fun getBirthNoteByPosition(
        textView: MaterialAutoCompleteTextView?,
        resources: Resources
    ): Int {
        val births = resources.getStringArray(R.array.birth_notes_values)
        return births.indexOf(textView?.text.toString()).takeIf { it != -1 }
            ?: births.size - 1
    }

    fun getGenderByPosition(
        selectedItemPosition: MaterialAutoCompleteTextView,
        resources: Resources
    ): Int {
        val genders = resources.getStringArray(R.array.gender_types_values)
        return genders.indexOf(selectedItemPosition.text.toString()).takeIf { it != -1 } ?: 0
    }

    fun getBirthNoteValue(note: Int, resources: Resources): String =
        resources.getStringArray(R.array.birth_notes_values)[note]

    fun getBreedingNoteValue(note: Int, resources: Resources): String =
        resources.getStringArray(R.array.breeding_notes_values)[note]

    fun getGenderByKey(genderId: Int, resources: Resources): String =
        resources.getStringArray(R.array.gender_types_values)[genderId]
}