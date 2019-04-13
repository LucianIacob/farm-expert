/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/13/19 10:47 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.model

import android.os.Bundle
import androidx.core.os.bundleOf
import com.farmexpert.android.dialogs.BaseEditRecordDialogFragment
import com.farmexpert.android.utils.FirestorePath
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Birth(
    @PropertyName(FirestorePath.Birth.CALF) val calfId: String = "",
    @PropertyName(FirestorePath.Birth.DATE_OF_BIRTH) val dateOfBirth: Timestamp = Timestamp.now(),
    @PropertyName(FirestorePath.Birth.MOTHER_ID) val motherId: String = "",
    @PropertyName(FirestorePath.Birth.NOTE) val note: String = "",
    @PropertyName(FirestorePath.Birth.CREATED_BY) val createdBy: String? = ""
) : BaseEntity() {

    @Exclude
    override fun getEditDialogArgs(): Bundle {
        return bundleOf(
            BaseEditRecordDialogFragment.EDIT_DIALOG_DATE to dateOfBirth.toDate().time,
            BaseEditRecordDialogFragment.EDIT_DIALOG_NOTE to note
        )
    }

}