/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 10/23/19 12:26 PM.
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

data class Breeding(
    @PropertyName(FirestorePath.Breeding.FEMALE) val female: String = "",
    @PropertyName(FirestorePath.Breeding.MALE) val male: String = "",
    @PropertyName(FirestorePath.Breeding.ACTION_DATE) val actionDate: Timestamp = Timestamp.now(),
    @PropertyName(FirestorePath.Breeding.NOTE) val note: Int = 5,
    @PropertyName(FirestorePath.Breeding.EXPECTED_BIRTH) val birthExpectedAt: Timestamp = Timestamp.now(),
    @PropertyName(FirestorePath.Breeding.CREATED_BY) val createdBy: String? = "",
    @PropertyName(FirestorePath.Breeding.LATEST_BREEDING) var latestBreeding: Boolean = false
) : BaseEntity() {

    @Exclude
    override fun getEditDialogArgs(): Bundle {
        return bundleOf(
            BaseEditRecordDialogFragment.EDIT_DIALOG_DOC_ID to id,
            BaseEditRecordDialogFragment.EDIT_DIALOG_DATE to actionDate.toDate().time,
            BaseEditRecordDialogFragment.EDIT_DIALOG_NOTE to note,
            BaseEditRecordDialogFragment.EDIT_DIALOG_MALE to male
        )
    }

}
