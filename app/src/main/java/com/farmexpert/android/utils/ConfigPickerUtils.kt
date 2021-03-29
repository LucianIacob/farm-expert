/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/15/19 1:01 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

import android.content.res.Resources
import com.farmexpert.android.R

object ConfigPickerUtils {

    fun getMaxValueByKey(key: String?, resources: Resources): Int {
        return when (key) {
            resources.getString(R.string.pref_heating_start_key) -> 21
            resources.getString(R.string.pref_heating_end_key) -> 25
            resources.getString(R.string.pref_gestation_key) -> 80
            resources.getString(R.string.pref_physiological_control_key) -> 40
            resources.getString(R.string.pref_disinfection_key) -> 70
            resources.getString(R.string.pref_vaccin_after_birth_key) -> 70
            resources.getString(R.string.pref_vaccin1_before_birth_key) -> 55
            resources.getString(R.string.pref_vaccin2_before_birth_key) -> 35
            resources.getString(R.string.pref_vaccin3_before_birth_key) -> 17
            resources.getString(R.string.pref_gestation_length_key) -> 300
            else -> 0
        }
    }

    fun getMinValueByKey(key: String?, resources: Resources): Int {
        return when (key) {
            resources.getString(R.string.pref_heating_start_key) -> 15
            resources.getString(R.string.pref_heating_end_key) -> 21
            resources.getString(R.string.pref_gestation_key) -> 40
            resources.getString(R.string.pref_physiological_control_key) -> 20
            resources.getString(R.string.pref_disinfection_key) -> 50
            resources.getString(R.string.pref_vaccin_after_birth_key) -> 50
            resources.getString(R.string.pref_vaccin1_before_birth_key) -> 36
            resources.getString(R.string.pref_vaccin2_before_birth_key) -> 18
            resources.getString(R.string.pref_vaccin3_before_birth_key) -> 5
            resources.getString(R.string.pref_gestation_length_key) -> 200
            else -> 0
        }
    }

    fun getDefaultValue(key: String?, resources: Resources?): Int {
        return when (key) {
            resources?.getString(R.string.pref_heating_start_key) -> 20
            resources?.getString(R.string.pref_heating_end_key) -> 22
            resources?.getString(R.string.pref_gestation_key) -> 60
            resources?.getString(R.string.pref_physiological_control_key) -> 30
            resources?.getString(R.string.pref_disinfection_key) -> 60
            resources?.getString(R.string.pref_vaccin_after_birth_key) -> 60
            resources?.getString(R.string.pref_vaccin1_before_birth_key) -> 45
            resources?.getString(R.string.pref_vaccin2_before_birth_key) -> 20
            resources?.getString(R.string.pref_vaccin3_before_birth_key) -> 15
            resources?.getString(R.string.pref_gestation_length_key) -> 285
            else -> 0
        }
    }

}
