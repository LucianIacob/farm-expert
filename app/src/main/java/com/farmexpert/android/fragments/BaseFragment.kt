/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/9/19 9:25 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.farmexpert.android.R
import com.farmexpert.android.activities.FarmSelectorActivity.Companion.KEY_CURRENT_FARM_ID
import com.farmexpert.android.activities.MainActivity
import com.farmexpert.android.utils.FirestorePath
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.longToast

open class BaseFragment : Fragment(), AnkoLogger {

    protected var currentUser: FirebaseUser? = null
    protected lateinit var farmReference: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val farmId = prefs.getString(KEY_CURRENT_FARM_ID, null)

        farmId?.let {
            currentUser = FirebaseAuth.getInstance().currentUser
            farmReference = FirebaseFirestore.getInstance()
                .collection(FirestorePath.Collections.FARMS)
                .document(farmId)
        } ?: kotlin.run {
            longToast(R.string.error_inexistent_farm_id)
            NavHostFragment.findNavController(this).popBackStack()
        }
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (nextAnim == 0) {
            onViewReady()
            return null
        }

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
        (activity as? MainActivity)?.setLoadingVisibility(View.VISIBLE)
    }

    fun loadingHide() {
        (activity as? MainActivity)?.setLoadingVisibility(View.GONE)
    }

    fun setTitle(title: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = title
    }
}
