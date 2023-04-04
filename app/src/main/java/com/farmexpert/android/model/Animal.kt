/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.model

import com.farmexpert.android.utils.FirestorePath
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Animal(
    @PropertyName(FirestorePath.Animal.RACE) val race: String = "",
    @PropertyName(FirestorePath.Animal.DATE_OF_BIRTH) val dateOfBirth: Timestamp = Timestamp.now(),
    @PropertyName(FirestorePath.Animal.GENDER) val gender: Int = 0,
    @PropertyName(FirestorePath.Animal.FATHER_ID) val fatherId: String = "",
    @PropertyName(FirestorePath.Animal.FATHER_FATHER_ID) val fatherFatherId: String = "",
    @PropertyName(FirestorePath.Animal.FATHER_MOTHER_ID) val fatherMotherId: String = "",
    @PropertyName(FirestorePath.Animal.MOTHER_ID) val motherId: String = "",
    @PropertyName(FirestorePath.Animal.MOTHER_FATHER_ID) val motherFatherId: String = "",
    @PropertyName(FirestorePath.Animal.MOTHER_MOTHER_ID) val motherMotherId: String = "",
    @PropertyName(FirestorePath.Animal.CREATED_BY) val createdBy: String? = "",
    @PropertyName(FirestorePath.Animal.LAST_DIGITS) val lastDigits: String? = ""
) : BaseEntity()
