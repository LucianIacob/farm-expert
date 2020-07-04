/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/18/19 9:50 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.farmexpert.android.R
import com.farmexpert.android.activities.ConfigurationActivity.Companion.FARM_TIMELINE_PREFS
import com.farmexpert.android.preference.NumberPickerPreference


/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, October 04, 2018.
 */
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        preferenceManager.sharedPreferencesName = FARM_TIMELINE_PREFS
        setPreferencesFromResource(R.xml.settings_prefs, p1)
    }

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        if (preference is NumberPickerPreference) {
            val dialogFragment =
                NumberPickerPreferenceDialogFragment.newInstance(preference.getKey())
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(parentFragmentManager, null)
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }

    override fun onStart() {
        super.onStart()
        initPreferenceSummaries()
    }

    private fun initPreferenceSummaries() {
        val prefScreen = preferenceScreen

        for (current in 0 until prefScreen.preferenceCount) {
            val preference = prefScreen.getPreference(current)
            preferenceChangeListener.onSharedPreferenceChanged(
                prefScreen.sharedPreferences,
                preference.key
            )
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
            .registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    private val preferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            val preference = findPreference<NumberPickerPreference>(key)
            val days = sharedPreferences.getInt(key, 1)
            preference?.summary = resources.getQuantityString(R.plurals.days, days, days)
        }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }
}