/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/11/19 8:11 AM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.app

import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.farmexpert.android.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

/**
 * Created by Lucian Iacob on March 10, 2019.
 */
class FarmExpertApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        initFirebaseApp()
        initStetho()
    }

    private fun initFirebaseApp() {
        FirebaseApp.initializeApp(this)
        val firestore = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        firestore.firestoreSettings = settings
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

}