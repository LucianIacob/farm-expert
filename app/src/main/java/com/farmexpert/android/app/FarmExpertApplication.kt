/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/10/19 1:48 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.app

import android.app.Application
import com.google.firebase.FirebaseApp

/**
 * Created by Lucian Iacob on March 10, 2019.
 */
class FarmExpertApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initFirebaseApp()
    }

    private fun initFirebaseApp() {
        FirebaseApp.initializeApp(this)
    }

}