/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/10/19 4:07 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.app.Activity
import android.os.Bundle
import com.farmexpert.android.R

class FarmSelectorActivity : Activity() {

    companion object {
        const val KEY_FARM_SELECTED = "com.farmexpert.android.SelectedFarmID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farm_selector)
    }

}
