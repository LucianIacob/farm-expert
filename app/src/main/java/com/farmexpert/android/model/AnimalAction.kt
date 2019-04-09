/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/9/19 9:25 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.model

import com.farmexpert.android.utils.FirestorePath
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.util.*

data class AnimalAction(
    @PropertyName(FirestorePath.AnimalAction.ANIMAL_ID) val animalId: String = "",
    @PropertyName(FirestorePath.AnimalAction.ACTION_DATE) val actionDate: Timestamp = Timestamp(Date()),
    @PropertyName(FirestorePath.AnimalAction.DETAILS) val details: String = "",
    @PropertyName(FirestorePath.AnimalAction.CREATED_BY) val createdBy: String? = ""
) : BaseEntity()