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
            BaseEditRecordDialogFragment.EDIT_DIALOG_DOC_ID to id,
            BaseDialogFragment.DIALOG_DATE to actionDate.toDate().time,
            BaseDialogFragment.DIALOG_DETAILS to details
        )
    }
}