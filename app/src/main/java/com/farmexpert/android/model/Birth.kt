/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.model

import android.os.Bundle
import androidx.core.os.bundleOf
import com.farmexpert.android.dialogs.BaseDialogFragment
import com.farmexpert.android.dialogs.BaseEditRecordDialogFragment
import com.farmexpert.android.utils.FirestorePath
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Birth(
    @PropertyName(FirestorePath.Birth.CALF) val calfId: String = "",
    @PropertyName(FirestorePath.Birth.DATE_OF_BIRTH) val dateOfBirth: Timestamp = Timestamp.now(),
    @PropertyName(FirestorePath.Birth.MOTHER_ID) val motherId: String = "",
    @PropertyName(FirestorePath.Birth.NOTE) val note: Int = 4,
    @PropertyName(FirestorePath.Birth.CREATED_BY) val createdBy: String? = "",
    @PropertyName(FirestorePath.Birth.LATEST_BIRTH) val latestBirth: Boolean = false,
    @PropertyName(FirestorePath.Birth.COMMENTS) val comments: String? = ""
) : BaseEntity() {

    @Exclude
    override fun getEditDialogArgs(): Bundle {
        return bundleOf(
            BaseEditRecordDialogFragment.EDIT_DIALOG_DOC_ID to id,
            BaseDialogFragment.DIALOG_DATE to dateOfBirth.toDate().time,
            BaseEditRecordDialogFragment.EDIT_DIALOG_NOTE to note,
            BaseDialogFragment.DIALOG_DETAILS to comments
        )
    }
}