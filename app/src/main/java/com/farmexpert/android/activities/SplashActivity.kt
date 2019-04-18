/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/18/19 9:50 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import androidx.preference.PreferenceManager
import com.farmexpert.android.R
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

/**
 * Created by Lucian Iacob on March 09, 2019.
 */
class SplashActivity : Activity() {

    companion object {
        const val SPLASH_LENGTH: Long = 2 * 1000
    }

    private lateinit var mHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mHandler = Handler()
    }

    override fun onResume() {
        super.onResume()
        gotoNextScreenAfterDelay()
    }

    private fun gotoNextScreenAfterDelay() {
        mHandler.postDelayed({
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
        }, SPLASH_LENGTH)
    }

    private fun userHasActiveFarm(): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        return prefs.getString(FarmSelectorActivity.KEY_CURRENT_FARM_ID, null) != null
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacksAndMessages(null)
    }

}
