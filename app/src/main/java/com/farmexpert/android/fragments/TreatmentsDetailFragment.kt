/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import androidx.navigation.fragment.navArgs
import com.farmexpert.android.R
import com.farmexpert.android.utils.FirestorePath

class TreatmentsDetailFragment : BaseAnimalActionDetailFragment() {

    override val getEditDialogTitle = R.string.edit_treatment_title

    override val getAddDialogTitle = R.string.add_treatment_title

    override val getTitleAndHolderLayout: Pair<Int, Int> =
        Pair(R.string.dashboard_graph_treatments, R.layout.item_animal_action)

    private val args: TreatmentsDetailFragmentArgs by navArgs()

    override fun getAnimalId() = args.animalId

    override fun getCollectionReference() =
        farmReference.collection(FirestorePath.Collections.TREATMENTS)
}