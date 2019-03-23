/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/23/19 10:56 AM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.farmexpert.android.activities.FarmSelectorActivity.Companion.KEY_CURRENT_FARM_ID
import com.farmexpert.android.activities.MainActivity

open class BaseFragment : Fragment() {

    lateinit var farmId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)

        farmId = prefs.getString(KEY_CURRENT_FARM_ID, "")!!
        // todo if farmID is empty, logout the user and re-login
    }

    fun loadingShow() {
        (activity as MainActivity).setLoadingVisibility(View.VISIBLE)
    }

    fun loadingHide() {
        (activity as MainActivity).setLoadingVisibility(View.GONE)
    }
}
