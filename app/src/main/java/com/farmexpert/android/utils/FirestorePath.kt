/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 7/17/19 10:02 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

object FirestorePath {

    object Collections {
        const val FARMS = "farms"
        const val ANIMALS = "animals"
        const val BIRTHS = "births"
        const val BREEDINGS = "breedings"
        const val DISINFECTIONS = "disinfections"
        const val PEDICURES = "pedicures"
        const val TREATMENTS = "treatments"
        const val VACCINATIONS = "vaccinations"
        const val VITAMINIZATIONS = "vitaminizations"
        const val REMINDERS = "reminders"
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
        const val FIRST_VACCINE = "vaccin1BeforeBirth"
        const val SECOND_VACCINE = "vaccin2BeforeBirth"
        const val THIRD_VACCINE = "vaccin3BeforeBirth"
        const val VACCINE_AFTER_BIRTH = "vaccinAfterBirth"
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
        const val LAST_DIGITS = "lastDigits"
    }

    object Birth {
        const val CALF = "calfId"
        const val DATE_OF_BIRTH = "dateOfBirth"
        const val MOTHER_ID = "motherId"
        const val NOTE = "note"
        const val CREATED_BY = "createdBy"
        const val LATEST_BIRTH = "latestBirth"
        const val COMMENTS = "comments"
    }

    object AnimalAction {
        const val ANIMAL_ID = "animalId"
        const val ACTION_DATE = "actionDate"
        const val DETAILS = "details"
        const val CREATED_BY = "createdBy"
    }

    object Breeding {
        const val NOTE = "note"
        const val ACTION_DATE = "actionDate"
        const val MALE = "male"
        const val FEMALE = "female"
        const val CREATED_BY = "createdBy"
        const val EXPECTED_BIRTH = "birthExpectedAt"
        const val LATEST_BREEDING = "latestBreeding"
        const val COMMENTS = "comments"
    }

    object Reminder {
        const val REMINDER_DATE = "reminderDate"
        const val DETAILS = "details"
        const val CREATED_BY = "createdBy"
        const val HOLDER_PARENT = "holderParent"
    }
}
