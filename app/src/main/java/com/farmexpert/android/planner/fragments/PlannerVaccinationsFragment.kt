/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 7/17/19 10:02 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.fragments

import com.farmexpert.android.R
import com.farmexpert.android.planner.model.PlannerContainer

class PlannerVaccinationsFragment : BasePlannerFragment() {

    override fun getPlannerContainer() = PlannerContainer.VACCINATIONS

    override fun getHeaderText(): String {
        return getString(R.string.planner_vaccinations_title)
    }

}