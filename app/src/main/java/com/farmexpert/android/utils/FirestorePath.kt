/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/8/19 1:42 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

object FirestorePath {

    object Collections {
        const val FARMS = "farms"
        const val ANIMALS = "animals"
    }

    object Farm {
        const val NAME = "name"
        const val OWNER = "owner"
        const val ACCESS_CODE = "accessCode"
        const val HEATING_START = "heatingStartsAt"
        const val HEATING_END = "heatingEndsAt"
        const val GESTATION_CONTROL = "gestationControl"
        const val PHYSIOLOGICAL_CONTROL = "physiologicalControl"
        const val DISINFECTION_BEFORE_BIRTH = "disinfectionBeforeBirth"
        const val FIRST_VACCIN = "vaccin1BeforeBirth"
        const val SECOND_VACCIN = "vaccin2BeforeBirth"
        const val THIRD_VACCIN = "vaccin3BeforeBirth"
        const val VACCIN_AFTER_BIRTH = "vaccinAfterBirth"
        const val GESTATION_LENGTH = "gestationLength"
        const val CREATION_DATE = "createdOn"
        const val USERS = "users"
    }

    object Animal {
        const val RACE = "race"
        const val DATE_OF_BIRTH = "dateOfBirth"
        const val GENDER = "gender"
        const val FATHER_ID = "fatherId"
        const val FATHER_FATHER_ID = "fatherFatherId"
        const val FATHER_MOTHER_ID = "fatherMotherId"
        const val MOTHER_ID = "motherId"
        const val MOTHER_FATHER_ID = "motherFatherId"
        const val MOTHER_MOTHER_ID = "motherMotherId"
        const val CREATED_BY = "createdBy"
    }

}
