/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 7/13/19 6:34 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.farmexpert.android.R
import com.farmexpert.android.activities.ConfigurationActivity
import com.farmexpert.android.app.FarmExpertApplication
import com.farmexpert.android.fragments.BaseFragment
import com.farmexpert.android.planner.adapter.PlannerAdapter
import com.farmexpert.android.viewmodel.PlannerDateViewModel
import kotlinx.android.synthetic.main.fragment_planner_section.*
import org.jetbrains.anko.support.v4.toast
import java.util.*

abstract class BasePlannerFragment : BaseFragment() {

    protected lateinit var adapter: PlannerAdapter

    private var plannerDateViewModel: PlannerDateViewModel? = null

    protected val farmTimelinePrefs: SharedPreferences =
        FarmExpertApplication.appContext.getSharedPreferences(
            ConfigurationActivity.FARM_TIMELINE_PREFS,
            Context.MODE_PRIVATE
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_planner_section, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        containerHeader.text = getHeaderText()
        PlannerAdapter { plannerItem -> toast(plannerItem.animalId) }.run {
            adapter = this
            plannerRecycler.adapter = this
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        parentFragment?.let {
            plannerDateViewModel = ViewModelProviders.of(it).get(PlannerDateViewModel::class.java)
        }
    }

    abstract fun getHeaderText(): String

    override fun onResume() {
        super.onResume()
        plannerDateViewModel?.getDate()
            ?.observe(this, Observer { date -> retrieveDataForDate(date) })
    }

    open fun retrieveDataForDate(date: Date) {

    }

    override fun onPause() {
        super.onPause()
        plannerDateViewModel?.getDate()?.removeObservers(this)
    }

}
