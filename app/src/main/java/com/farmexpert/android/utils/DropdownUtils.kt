/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
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
            ?.let { it + 1 }
            ?: breedings.size
    }

    fun getBirthNoteByPosition(
        textView: MaterialAutoCompleteTextView?,
        resources: Resources
    ): Int {
        val births = resources.getStringArray(R.array.birth_notes_values)
        return births.indexOf(textView?.text.toString()).takeIf { it != -1 }
            ?.let { it + 1 }
            ?: births.size
    }

    fun getGenderByPosition(
        selectedItemPosition: MaterialAutoCompleteTextView,
        resources: Resources
    ): Int {
        val genders = resources.getStringArray(R.array.gender_types_values)
        return genders.indexOf(selectedItemPosition.text.toString()).takeIf { it != -1 }
            ?.let { it + 1 } ?: 0
    }

    fun getBirthNoteValue(note: Int, resources: Resources): String =
        resources.getStringArray(R.array.birth_notes_values)[note - 1]

    fun getBreedingNoteValue(note: Int, resources: Resources): String =
        resources.getStringArray(R.array.breeding_notes_values)[note - 1]

    fun getGenderByKey(genderId: Int, resources: Resources): String =
        resources.getStringArray(R.array.gender_types_values)[genderId - 1]
}