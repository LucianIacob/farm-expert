/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/13/19 9:05 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, 05 January, 2018.
 */

object FarmValidator {
    fun isValidAnimalId(id: String) = id.length >= 4
}
