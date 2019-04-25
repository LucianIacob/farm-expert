/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/25/19 5:10 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.farmexpert.android.R
import com.farmexpert.android.fragments.BaseFragment
import com.farmexpert.android.utils.getShort
import com.farmexpert.android.viewmodel.PlannerDateViewModel
import kotlinx.android.synthetic.main.fragment_planner.*

class PlannerFragment : BaseFragment() {

    private lateinit var mPlannerDateViewModel: PlannerDateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_planner, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inflateFragments()
        mPlannerDateViewModel = ViewModelProviders.of(this).get(PlannerDateViewModel::class.java)
//        if (PlannerConstants.SHOULD_RESET_PLANNER_DATE) {
//            mPlannerDateViewModel.setDate(Date())
//        } else {
//            PlannerConstants.SHOULD_RESET_PLANNER_DATE = true
//        }
    }

    private fun inflateFragments() {
        inflateFragment(HeatsPlannerFragment(), R.id.heatsContainer)
        inflateFragment(PlannerReproductionControlFragment(), R.id.reprodControlContainer)
        inflateFragment(PlannerVaccinationsFragment(), R.id.vaccinationsContainer)
        inflateFragment(PlannerDisinfectionsFragment(), R.id.disinfectionsContainer)
    }

    private fun inflateFragment(fragment: Fragment, layoutId: Int) {
        childFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(layoutId, fragment)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        prevDayBtn.setOnClickListener { mPlannerDateViewModel.prevDay() }
        nextDayBtn.setOnClickListener { mPlannerDateViewModel.nextDay() }
        mPlannerDateViewModel.getDate().observe(this,
            Observer { date -> currentDay.text = date.getShort() })
    }

    override fun onPause() {
        super.onPause()
        mPlannerDateViewModel.getDate().removeObservers(this)
    }
}
