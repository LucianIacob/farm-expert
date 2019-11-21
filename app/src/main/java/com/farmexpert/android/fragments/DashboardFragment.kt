/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/23/19 7:00 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.ContentViewEvent
import com.farmexpert.android.R
import com.farmexpert.android.adapter.DashboardAdapter
import com.farmexpert.android.model.DashboardItem
import kotlinx.android.synthetic.main.fragment_dashboard.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, February 19, 2018.
 */
class DashboardFragment : Fragment() {

    private val clickListener: (DashboardItem) -> Unit = { dashboardItem -> onItemClick(dashboardItem) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

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
        setupAdapter()
    }

    private fun setupAdapter() {
        val columnsCount = resources.getInteger(R.integer.dashboard_columns_count)
        recycler_dashboard.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, columnsCount)
        recycler_dashboard.adapter = DashboardAdapter(DashboardItem.values(), clickListener)
    }

    private fun onItemClick(dashboardItem: DashboardItem) {
        NavHostFragment.findNavController(this).navigate(dashboardItem.actionDestination)
        Answers.getInstance().logContentView(ContentViewEvent().putContentName(dashboardItem.name))
    }
}