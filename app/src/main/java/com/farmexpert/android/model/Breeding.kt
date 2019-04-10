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

data class Breeding(
    @PropertyName(FirestorePath.Breeding.FEMALE) val female: String = "",
    @PropertyName(FirestorePath.Breeding.MALE) val male: String = "",
    @PropertyName(FirestorePath.Breeding.ACTION_DATE) val actionDate: Timestamp = Timestamp.now(),
    @PropertyName(FirestorePath.Breeding.NOTE) val note: String = "",
    @PropertyName(FirestorePath.Breeding.EXPECTED_BIRTH) val birthExpectedAt: Timestamp = Timestamp.now(),
    @PropertyName(FirestorePath.Breeding.CREATED_BY) val createdBy: String? = ""
) : BaseEntity()
