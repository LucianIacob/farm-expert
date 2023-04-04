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
import java.util.*

data class Farm(
    @PropertyName(FirestorePath.Farm.OWNER) val owner: String? = "",
    @PropertyName(FirestorePath.Farm.NAME) val name: String = "",
    @PropertyName(FirestorePath.Farm.ACCESS_CODE) val accessCode: String = "",
    @PropertyName(FirestorePath.Farm.HEATING_START) val heatingStartsAt: Int = 20,
    @PropertyName(FirestorePath.Farm.HEATING_END) val heatingEndsAt: Int = 22,
    @PropertyName(FirestorePath.Farm.GESTATION_CONTROL) val gestationControl: Int = 60,
    @PropertyName(FirestorePath.Farm.PHYSIOLOGICAL_CONTROL) val physiologicalControl: Int = 30,
    @PropertyName(FirestorePath.Farm.DISINFECTION_BEFORE_BIRTH) val disinfectionBeforeBirth: Int = 60,
    @PropertyName(FirestorePath.Farm.FIRST_VACCINE) val vaccin1BeforeBirth: Int = 45,
    @PropertyName(FirestorePath.Farm.SECOND_VACCINE) val vaccin2BeforeBirth: Int = 20,
    @PropertyName(FirestorePath.Farm.THIRD_VACCINE) val vaccin3BeforeBirth: Int = 15,
    @PropertyName(FirestorePath.Farm.VACCINE_AFTER_BIRTH) val vaccinAfterBirth: Int = 60,
    @PropertyName(FirestorePath.Farm.GESTATION_LENGTH) val gestationLength: Int = 285,
    @PropertyName(FirestorePath.Farm.USERS) val users: ArrayList<String?> = arrayListOf(),
    @PropertyName(FirestorePath.Farm.CREATION_DATE) val createdOn: Timestamp = Timestamp.now()
) : BaseEntity()
