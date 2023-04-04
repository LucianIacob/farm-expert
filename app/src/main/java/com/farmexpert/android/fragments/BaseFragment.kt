/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.farmexpert.android.R
import com.farmexpert.android.activities.FarmSelectorActivity
import com.farmexpert.android.activities.FarmSelectorActivity.Companion.KEY_CURRENT_FARM_ID
import com.farmexpert.android.activities.MainActivity
import com.farmexpert.android.utils.FirestorePath
import com.farmexpert.android.utils.longToast
import com.farmexpert.android.utils.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

open class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    protected var currentUser: FirebaseUser? = null
    protected lateinit var farmReference: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceManager.getDefaultSharedPreferences(activity)
            .getString(KEY_CURRENT_FARM_ID, null)
            ?.let { farmId ->
                currentUser = FirebaseAuth.getInstance().currentUser
                farmReference = Firebase.firestore
                    .collection(FirestorePath.Collections.FARMS)
                    .document(farmId)
            }
            ?: run {
                longToast(R.string.error_inexistent_farm_id)
                if (NavHostFragment.findNavController(this).popBackStack().not()) {
                    startActivity<FarmSelectorActivity>()
                    activity?.finish()
                }
            }
    }

    fun loadingShow() {
        (activity as? MainActivity)?.setLoadingVisibility(true)
    }

    fun loadingHide() {
        (activity as? MainActivity)?.setLoadingVisibility(false)
    }

    fun setTitle(title: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = title
    }
}
