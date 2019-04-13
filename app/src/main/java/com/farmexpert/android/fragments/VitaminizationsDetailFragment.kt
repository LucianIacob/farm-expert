/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/13/19 11:14 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import androidx.navigation.fragment.navArgs
import com.farmexpert.android.R
import com.farmexpert.android.utils.FirestorePath

class VitaminizationsDetailFragment : BaseAnimalActionDetailFragment() {

    override fun getTitleAndHolderLayout(): Pair<String, Int> =
        Pair(getString(R.string.dashboard_graph_vitaminisations), R.layout.item_animal_action)

    private val args: VitaminizationsDetailFragmentArgs by navArgs()

    override fun getAnimalId() = args.animalId

    override fun getCollectionReference() =
        farmReference.collection(FirestorePath.Collections.VITAMINIZATIONS)

    override fun getAddDialogTitle() = R.string.add_vitaminization_title

    override fun getEditDialogTitle() = R.string.edit_vitaminization_title
}