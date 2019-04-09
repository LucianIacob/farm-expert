/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/9/19 9:25 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.farmexpert.android.R
import com.farmexpert.android.adapter.holder.BirthHolder
import com.farmexpert.android.model.Birth
import com.farmexpert.android.utils.FirestorePath
import com.firebase.ui.firestore.SnapshotParser

class VaccinationsDetailFragment : BaseDetailFragment<Birth, BirthHolder>() {

    override val snapshotParser: SnapshotParser<Birth>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun createHolder(view: View): BirthHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTitleAndHolderLayout(): Pair<String, Int> =
        Pair(getString(R.string.dashboard_graph_disinfections), R.layout.item_birth)

    private val args: VaccinationsDetailFragmentArgs by navArgs()

    override fun getAnimalId() = args.animalId

    override fun getAddRecordDialog() = DialogFragment()

    override fun getCollectionReference() =
        farmReference.collection(FirestorePath.Collections.VACCINATIONS)

}