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
import java.util.*

data class AnimalAction(
    @PropertyName(FirestorePath.AnimalAction.ANIMAL_ID) val animalId: String = "",
    @PropertyName(FirestorePath.AnimalAction.ACTION_DATE) val actionDate: Timestamp = Timestamp(Date()),
    @PropertyName(FirestorePath.AnimalAction.DETAILS) val details: String = "",
    @PropertyName(FirestorePath.AnimalAction.CREATED_BY) val createdBy: String? = ""
) : BaseEntity() {

    @Exclude
    override fun getEditDialogArgs(): Bundle {
        return bundleOf(
            BaseEditRecordDialogFragment.EDIT_DIALOG_DATE to actionDate.toDate().time,
            BaseEditRecordDialogFragment.EDIT_DIALOG_DETAILS to details
        )
    }

}