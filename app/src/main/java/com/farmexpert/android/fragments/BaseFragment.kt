/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/6/19 7:37 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.farmexpert.android.activities.FarmSelectorActivity.Companion.KEY_CURRENT_FARM_ID
import com.farmexpert.android.activities.MainActivity
import org.jetbrains.anko.AnkoLogger

open class BaseFragment : Fragment(), AnkoLogger {

    lateinit var farmId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)

        farmId = prefs.getString(KEY_CURRENT_FARM_ID, "")!!
        // todo if farmID is empty, logout the user and re-login
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return AnimationUtils.loadAnimation(activity, nextAnim).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                    // not used
                }

                override fun onAnimationEnd(animation: Animation?) {
                    if (enter) onViewReady()
                }

                override fun onAnimationStart(animation: Animation?) {
                    // not used
                }
            })
        }
    }

    protected open fun onViewReady() {
        // may be overridden by subclasses
    }

    fun loadingShow() {
        (activity as MainActivity).setLoadingVisibility(View.VISIBLE)
    }

    fun loadingHide() {
        (activity as MainActivity).setLoadingVisibility(View.GONE)
    }

    fun setTitle(title: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = title
    }
}
