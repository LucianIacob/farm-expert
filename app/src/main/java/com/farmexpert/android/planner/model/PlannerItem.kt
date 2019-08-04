/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 8/4/19 10:52 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.model

import androidx.navigation.NavDirections

data class PlannerItem(
    val headline: String? = null,
    val reason: String? = null,
    val onClickAction: NavDirections? = null,
    val longClickId: String? = null
)
