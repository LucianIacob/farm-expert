/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/18/19 9:50 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.farmexpert.android.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity

/**
 * Created by Lucian Iacob on March 09, 2019.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            delay(SPLASH_LENGTH)
            gotoNextScreen()
        }
    }

    private fun gotoNextScreen() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            if (userHasActiveFarm()) {
                if (userHasReviewedFarmConfigs()) { // TODO  SAVE THIS  FLAG On THE BACKEND
                    startActivity<MainActivity>()
                } else {
                    startActivity<ConfigurationActivity>()
                }
            } else {
                startActivity<FarmSelectorActivity>()
            }
        } else {
            startActivity<AuthenticationActivity>()
        }

        finish()
    }

    private fun userHasActiveFarm() =
        PreferenceManager
            .getDefaultSharedPreferences(this)
            .getString(FarmSelectorActivity.KEY_CURRENT_FARM_ID, null) != null

    private fun userHasReviewedFarmConfigs() =
        PreferenceManager
            .getDefaultSharedPreferences(this)
            .getBoolean(ConfigurationActivity.KEY_CONFIGS_ACCEPTED, false)

    companion object {
        const val SPLASH_LENGTH: Long = 2 * 1000
    }
}
