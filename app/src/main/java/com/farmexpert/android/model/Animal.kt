/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/23/19 7:10 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.model

import com.farmexpert.android.utils.FirestorePath
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import java.util.*

data class Animal(
    @PropertyName(FirestorePath.Animal.RACE) val race: String,
    @PropertyName(FirestorePath.Animal.DATE_OF_BIRTH) val dateOfBirth: Timestamp = Timestamp(Date()),
    @PropertyName(FirestorePath.Animal.GENRE) val genre: String,
    @PropertyName(FirestorePath.Animal.FATHER_ID) val fatherId: String,
    @PropertyName(FirestorePath.Animal.MOTHER_ID) val motherId: String,
    @PropertyName(FirestorePath.Animal.CREATED_BY) val createdBy: String
) {

    @Suppress("unused") // used by Firestore
    constructor() : this("", Timestamp(Date()), "", "", "", "")

    @Exclude
    var id: String? = null
        @Exclude get() {
            return field
        }
        @Exclude set(value) {
            field = value
        }

}
