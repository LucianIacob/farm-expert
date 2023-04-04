/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
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
