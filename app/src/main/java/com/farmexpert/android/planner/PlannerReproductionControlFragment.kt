/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/25/19 8:58 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner

import com.farmexpert.android.R

class PlannerReproductionControlFragment : BasePlannerFragment() {

    override fun getHeaderText(): String {
        return getString(R.string.planner_reprod_control_title)
    }

}
