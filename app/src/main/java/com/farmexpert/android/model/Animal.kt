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

data class Animal(
    @PropertyName(FirestorePath.Animal.RACE) val race: String = "",
    @PropertyName(FirestorePath.Animal.DATE_OF_BIRTH) val dateOfBirth: Timestamp = Timestamp(Date()),
    @PropertyName(FirestorePath.Animal.GENDER) val gender: String,
    @PropertyName(FirestorePath.Animal.FATHER_ID) val fatherId: String = "",
    @PropertyName(FirestorePath.Animal.FATHER_FATHER_ID) val fatherFatherId: String = "",
    @PropertyName(FirestorePath.Animal.FATHER_MOTHER_ID) val fatherMotherId: String = "",
    @PropertyName(FirestorePath.Animal.MOTHER_ID) val motherId: String,
    @PropertyName(FirestorePath.Animal.MOTHER_FATHER_ID) val motherFatherId: String = "",
    @PropertyName(FirestorePath.Animal.MOTHER_MOTHER_ID) val motherMotherId: String = "",
    @PropertyName(FirestorePath.Animal.CREATED_BY) val createdBy: String?
) : BaseEntity() {

    @Suppress("unused") // used by Firestore
    constructor() : this(
        race = "",
        dateOfBirth = Timestamp(Date()),
        gender = "",
        fatherId = "",
        motherId = "",
        createdBy = ""
    )

}
