/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/15/19 12:14 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.app

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.facebook.stetho.Stetho
import com.farmexpert.android.BuildConfig
import com.google.firebase.FirebaseApp
import io.fabric.sdk.android.Fabric

/**
 * Created by Lucian Iacob on March 10, 2019.
 */
class FarmExpertApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        sInstance = this
        initFirebaseApp()
        initStetho()
        setupFabric()
    }

    private fun initFirebaseApp() {
        FirebaseApp.initializeApp(this)
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    private fun setupFabric() {
        Fabric.with(this, Crashlytics(), Answers())
    }

    companion object {
        private lateinit var sInstance: FarmExpertApplication

        val appContext: Context
            get() = sInstance.applicationContext
    }
}