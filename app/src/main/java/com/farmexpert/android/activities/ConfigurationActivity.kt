/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/18/19 9:50 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.farmexpert.android.R
import com.farmexpert.android.utils.ConfigPickerUtils
import com.farmexpert.android.utils.FirestorePath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_configuration.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity

class ConfigurationActivity : AppCompatActivity(), AnkoLogger {

    companion object {
        const val FARM_TIMELINE_PREFS = "com.farmexpert.android.farm_timeline_settings"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.config_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_finish) {
            updateFarmDetails()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun updateFarmDetails() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val farmId = prefs.getString(FarmSelectorActivity.KEY_CURRENT_FARM_ID, "")

        if (farmId.isNullOrEmpty()) {
            longToast(R.string.unknown_error)
            return
        }

        val sharedPrefs = getSharedPreferences(FARM_TIMELINE_PREFS, Context.MODE_PRIVATE)

        Firebase.firestore
            .collection(FirestorePath.Collections.FARMS)
            .document(farmId)
            .update(
                FirestorePath.Farm.HEATING_START, sharedPrefs.getInt(
                    getString(R.string.pref_heating_start_key),
                    ConfigPickerUtils.getDefaultValue(
                        getString(R.string.pref_heating_start_key),
                        resources
                    )
                ),
                FirestorePath.Farm.HEATING_END, sharedPrefs.getInt(
                    getString(R.string.pref_heating_end_key),
                    ConfigPickerUtils.getDefaultValue(
                        getString(R.string.pref_heating_end_key),
                        resources
                    )
                ),
                FirestorePath.Farm.GESTATION_CONTROL, sharedPrefs.getInt(
                    getString(R.string.pref_gestation_key),
                    ConfigPickerUtils.getDefaultValue(
                        getString(R.string.pref_gestation_key),
                        resources
                    )
                ),
                FirestorePath.Farm.PHYSIOLOGICAL_CONTROL, sharedPrefs.getInt(
                    getString(R.string.pref_physiological_control_key),
                    ConfigPickerUtils.getDefaultValue(
                        getString(R.string.pref_physiological_control_key),
                        resources
                    )
                ),
                FirestorePath.Farm.DISINFECTION_BEFORE_BIRTH, sharedPrefs.getInt(
                    getString(R.string.pref_disinfection_key),
                    ConfigPickerUtils.getDefaultValue(
                        getString(R.string.pref_disinfection_key),
                        resources
                    )
                ),
                FirestorePath.Farm.FIRST_VACCINE, sharedPrefs.getInt(
                    getString(R.string.pref_vaccin1_before_birth_key),
                    ConfigPickerUtils.getDefaultValue(
                        getString(R.string.pref_vaccin1_before_birth_key),
                        resources
                    )
                ),
                FirestorePath.Farm.SECOND_VACCINE, sharedPrefs.getInt(
                    getString(R.string.pref_vaccin2_before_birth_key),
                    ConfigPickerUtils.getDefaultValue(
                        getString(R.string.pref_vaccin2_before_birth_key),
                        resources
                    )
                ),
                FirestorePath.Farm.THIRD_VACCINE, sharedPrefs.getInt(
                    getString(R.string.pref_vaccin3_before_birth_key),
                    ConfigPickerUtils.getDefaultValue(
                        getString(R.string.pref_vaccin3_before_birth_key),
                        resources
                    )
                ),
                FirestorePath.Farm.VACCINE_AFTER_BIRTH, sharedPrefs.getInt(
                    getString(R.string.pref_vaccin_after_birth_key),
                    ConfigPickerUtils.getDefaultValue(
                        getString(R.string.pref_vaccin_after_birth_key),
                        resources
                    )
                ),
                FirestorePath.Farm.GESTATION_LENGTH, sharedPrefs.getInt(
                    getString(R.string.pref_gestation_length_key),
                    ConfigPickerUtils.getDefaultValue(
                        getString(R.string.pref_gestation_length_key),
                        resources
                    )
                )
            )
            .addOnSuccessListener { startMainActivity() }
            .addOnFailureListener {
                longToast(R.string.unknown_error)
                error { it }
            }
    }

    private fun startMainActivity() {
        startActivity<MainActivity>()
        finish()
    }

}
