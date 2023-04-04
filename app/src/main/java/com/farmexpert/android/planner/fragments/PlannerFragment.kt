/*
 * Developed by Lucian Iacob.
 * Romania, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:35 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.farmexpert.android.R
import com.farmexpert.android.fragments.BaseFragment
import com.farmexpert.android.utils.NavigationConstants
import com.farmexpert.android.utils.getShort
import com.farmexpert.android.viewmodel.PlannerDateViewModel
import kotlinx.android.synthetic.main.fragment_planner.*
import java.util.*

class PlannerFragment : BaseFragment(R.layout.fragment_planner) {

    private val plannerDateViewModel by viewModels<PlannerDateViewModel>()

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        inflateFragments()

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
        childFragmentManager.commit {
            setTransition(FragmentTransaction.TRANSIT_NONE)
            replace(layoutId, fragment)
        }
    }

    override fun onResume() {
        super.onResume()
        prevDayBtn.setOnClickListener { plannerDateViewModel.prevDaySelected() }
        nextDayBtn.setOnClickListener { plannerDateViewModel.nextDaySelected() }
        plannerDateViewModel.getDate()
            .observe(this, { date -> currentDay.text = date.getShort() })
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
