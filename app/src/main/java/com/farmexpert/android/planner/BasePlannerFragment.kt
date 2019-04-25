/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/25/19 8:58 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.farmexpert.android.R
import com.farmexpert.android.fragments.BaseFragment
import com.farmexpert.android.viewmodel.PlannerDateViewModel
import kotlinx.android.synthetic.main.fragment_planner_section.*

abstract class BasePlannerFragment : BaseFragment() {

    private var plannerDateViewModel: PlannerDateViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_planner_section, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        containerHeader.text = getHeaderText()
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
        plannerDateViewModel?.getDate()?.observe(this, Observer { date -> })
    }

    override fun onPause() {
        super.onPause()
        plannerDateViewModel?.getDate()?.removeObservers(this)
    }

}
