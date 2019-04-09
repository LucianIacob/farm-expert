/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/9/19 9:30 AM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import androidx.navigation.fragment.navArgs
import com.farmexpert.android.R

class BirthsDetailFragment : BaseDetailFragment() {

    private val args: BirthsDetailFragmentArgs by navArgs()

    override fun getAnimalId() = args.animalId

    override fun getTitle(): String = getString(R.string.dashboard_graph_births)

}