/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 7/13/19 6:34 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.fragments

import com.farmexpert.android.R

class PlannerVaccinationsFragment : BasePlannerFragment() {

    override fun getHeaderText(): String {
        return getString(R.string.planner_vaccinations_title)
    }

}
