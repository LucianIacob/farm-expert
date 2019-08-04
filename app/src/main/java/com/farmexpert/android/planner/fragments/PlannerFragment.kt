/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 8/5/19 8:57 AM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.fragments;

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
import com.farmexpert.android.utils.NavigationConstants
import com.farmexpert.android.utils.getShort
import com.farmexpert.android.viewmodel.PlannerDateViewModel
import kotlinx.android.synthetic.main.fragment_planner.*
import java.util.*

class PlannerFragment : BaseFragment() {

    private lateinit var plannerDateViewModel: PlannerDateViewModel

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
        plannerDateViewModel = ViewModelProviders.of(this).get(PlannerDateViewModel::class.java)

        (savedInstanceState?.getSerializable(KEY_SELECTED_DATE) as? Date)?.let {
            plannerDateViewModel.setDate(it)
        }

        if (NavigationConstants.SHOULD_RESET_PLANNER_DATE) {
            plannerDateViewModel.setDate(Date())
        } else {
            NavigationConstants.SHOULD_RESET_PLANNER_DATE = true
        }
    }

    private fun inflateFragments() {
        inflateFragment(PlannerHeatsFragment(), R.id.heatsContainer)
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
        prevDayBtn.setOnClickListener { plannerDateViewModel.prevDay() }
        nextDayBtn.setOnClickListener { plannerDateViewModel.nextDay() }
        plannerDateViewModel.getDate()
            .observe(this, Observer { date -> currentDay.text = date.getShort() })
    }

    override fun onPause() {
        super.onPause()
        plannerDateViewModel.getDate().removeObservers(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_SELECTED_DATE, plannerDateViewModel.getDate().value)
        NavigationConstants.SHOULD_RESET_PLANNER_DATE = false
    }

    companion object {
        const val KEY_SELECTED_DATE = "com.farmexpert.android.Planner.SelectedDate"
    }
}
