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
import com.google.firebase.firestore.CollectionReference

class DisinfectionsDetailFragment : BaseAnimalActionDetailFragment() {

    override fun getTitleAndHolderLayout(): Pair<String, Int> =
        Pair(getString(R.string.dashboard_graph_disinfections), R.layout.item_animal_action)

    private val args: DisinfectionsDetailFragmentArgs by navArgs()

    override fun getAnimalId() = args.animalId

    override fun getAddDialogTitle() = R.string.add_disinfection_title

    override fun getCollectionReference(): CollectionReference {
        return farmReference.collection(FirestorePath.Collections.DISINFECTIONS)
    }

    override fun getEditDialogTitle() = R.string.edit_disinfection_title
}