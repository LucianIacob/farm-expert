/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/18/19 9:50 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.farmexpert.android.R
import com.farmexpert.android.utils.startActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by Lucian Iacob on March 09, 2019.
 */
class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

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
                startActivity<MainActivity>()
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

    companion object {
        const val SPLASH_LENGTH: Long = 2 * 1000
    }
}
