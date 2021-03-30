/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/19/19 9:36 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import androidx.navigation.fragment.navArgs
import com.farmexpert.android.R
import com.farmexpert.android.utils.FirestorePath

class DisinfectionsDetailFragment : BaseAnimalActionDetailFragment() {

    override val getTitleAndHolderLayout: Pair<Int, Int> =
        Pair(R.string.dashboard_graph_disinfections, R.layout.item_animal_action)

    override val getAddDialogTitle = R.string.add_disinfection_title

    override val getEditDialogTitle = R.string.edit_disinfection_title

    private val args: DisinfectionsDetailFragmentArgs by navArgs()

    override fun getAnimalId() = args.animalId

    override fun getCollectionReference() =
        farmReference.collection(FirestorePath.Collections.DISINFECTIONS)
}