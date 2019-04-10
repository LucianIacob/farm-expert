/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/10/19 9:18 AM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.model

import com.farmexpert.android.utils.FirestorePath
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Birth(
    @PropertyName(FirestorePath.Birth.CALF) val calfId: String = "",
    @PropertyName(FirestorePath.Birth.DATE_OF_BIRTH) val dateOfBirth: Timestamp = Timestamp.now(),
    @PropertyName(FirestorePath.Birth.MOTHER_ID) val motherId: String = "",
    @PropertyName(FirestorePath.Birth.NOTE) val note: String = "",
    @PropertyName(FirestorePath.Birth.CREATED_BY) val createdBy: String? = ""
) : BaseEntity()