/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

abstract class BaseAddRecordDialogFragment : BaseDialogFragment() {

    companion object {
        const val ADD_DIALOG_CALF = "com.farmexpert.android.CalfID"
        const val ADD_DIALOG_NOTE = "com.farmexpert.android.Note"
        const val ADD_DIALOG_ANIMAL = "com.farmexpert.android.AnimalId"
        const val ADD_DIALOG_GENDER = "com.farmexpert.android.Gender"
        const val ADD_DIALOG_RACE = "com.farmexpert.android.Race"
        const val ADD_DIALOG_FATHER = "com.farmexpert.android.FatherId"
        const val ADD_DIALOG_MOTHER = "com.farmexpert.android.MotherId"
        const val ADD_DIALOG_DETAILS = "com.farmexpert.android.Details"
        const val ADD_DIALOG_MALE = "com.farmexpert.android.Male"
    }
}