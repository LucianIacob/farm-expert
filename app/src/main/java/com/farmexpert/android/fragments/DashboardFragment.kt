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
import android.view.View
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.farmexpert.android.R
import com.farmexpert.android.adapter.DashboardAdapter
import com.farmexpert.android.model.DashboardItem
import kotlinx.android.synthetic.main.fragment_dashboard.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, February 19, 2018.
 */
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val clickListener: (DashboardItem) -> Unit =
        { dashboardItem -> onItemClick(dashboardItem) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit {
                remove(AnimalMasterFragment.KEY_LAYOUT_STATE)
                remove(BaseMasterFragment.KEY_LAYOUT_STATE)
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler_dashboard.adapter = DashboardAdapter(DashboardItem.values(), clickListener)
    }

    private fun onItemClick(dashboardItem: DashboardItem) {
        NavHostFragment.findNavController(this).navigate(dashboardItem.actionDestination)
    }
}