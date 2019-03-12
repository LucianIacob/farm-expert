/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/12/19 12:01 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.model

import com.farmexpert.android.utils.FirestorePath
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import java.util.*

data class Farm(
    @PropertyName(FirestorePath.Farm.OWNER) val owner: String?,
    @PropertyName(FirestorePath.Farm.NAME) val name: String,
    @PropertyName(FirestorePath.Farm.ACCESS_CODE) val accessCode: String,
    @PropertyName(FirestorePath.Farm.USERS) val users: ArrayList<String?>,
    @PropertyName(FirestorePath.Farm.CREATION_DATE) val createdOn: Timestamp = Timestamp(Date())
) {

    @Suppress("unused") // used by Firestore
    constructor() : this("", "", "", arrayListOf(), Timestamp(Date()))

    @Exclude
    var id: String? = null
        @Exclude get() {
            return field
        }
        @Exclude set(value) {
            field = value
        }

}
