/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/7/19 10:12 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.farmexpert.android.R
import com.farmexpert.android.model.Animal
import com.farmexpert.android.utils.FirestorePath
import com.farmexpert.android.utils.getShort
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_animal_detail.*
import org.jetbrains.anko.support.v4.toast

class AnimalDetailFragment : BaseFragment() {

    private val args: AnimalDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animal_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animalId.text = args.id
        fatherIdView.setExpandListener(viewToExpand = fatherParentsContainer)
        motherIdView.setExpandListener(viewToExpand = motherParentsContainer)
    }

    override fun onViewReady() {
        loadingShow()
        FirebaseFirestore.getInstance()
            .collection(FirestorePath.Collections.FARMS)
            .document(farmId)
            .collection(FirestorePath.Collections.ANIMALS)
            .document(args.id)
            .get()
            .addOnFailureListener { toast(R.string.unknown_error) }
            .addOnSuccessListener { populateUi(it.toObject(Animal::class.java)) }
            .addOnCompleteListener { loadingHide() }
    }

    private fun populateUi(animal: Animal?) {
        animal?.let {
            raceView.setValue(it.race)
            dateOfBirthView.setValue(it.dateOfBirth.toDate().getShort())
            genreView.setValue(it.genre)
            fatherIdView.setValue(it.fatherId)
            motherIdView.setValue(it.motherId)
        }
    }

}