/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/15/19 9:22 AM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

object FirestorePath {

    object Collections {
        const val FARMS = "farms"
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

}
