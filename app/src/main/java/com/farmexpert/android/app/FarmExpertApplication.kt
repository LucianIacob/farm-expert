/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.app

import android.app.Application
import com.facebook.stetho.Stetho
import com.farmexpert.android.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * Created by Lucian Iacob on March 10, 2019.
 */
class FarmExpertApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initFirebaseApp()
        initStetho()
        setupCrashlytics()
    }

    private fun initFirebaseApp() {
        FirebaseApp.initializeApp(this)
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    private fun setupCrashlytics() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            FirebaseCrashlytics.getInstance().setUserId(it)
        }
        FirebaseAuth.getInstance().currentUser?.email?.let {
            FirebaseCrashlytics.getInstance().setCustomKey("user_email", it)
        }
        FirebaseAuth.getInstance().currentUser?.phoneNumber?.let {
            FirebaseCrashlytics.getInstance().setCustomKey("phone_number", it)
        }
        FirebaseAuth.getInstance().currentUser?.displayName?.let {
            FirebaseCrashlytics.getInstance().setCustomKey("user_name", it)
        }
        FirebaseAuth.getInstance().currentUser?.providerId?.let {
            FirebaseCrashlytics.getInstance().setCustomKey("provider_id", it)
        }
        FirebaseAuth.getInstance().currentUser?.tenantId?.let {
            FirebaseCrashlytics.getInstance().setCustomKey("tenant_id", it)
        }
    }
}