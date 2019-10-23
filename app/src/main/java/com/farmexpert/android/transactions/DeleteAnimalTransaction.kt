/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/11/19 8:37 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.transactions

import com.farmexpert.android.utils.FirestorePath
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class DeleteAnimalTransaction(
    val farmReference: DocumentReference,
    val successListener: () -> Unit,
    val failureListener: (Exception) -> Unit,
    val complete: () -> Unit
) : AnkoLogger {

    private var birthsToDelete = listOf<DocumentReference>()
    private var breedingsToDelete = listOf<DocumentReference>()
    private var pedicuresToDelete = listOf<DocumentReference>()
    private var treatmentsToDelete = listOf<DocumentReference>()
    private var vaccinationsToDelete = listOf<DocumentReference>()
    private var disinfectionsToDelete = listOf<DocumentReference>()
    private var vitaminizationsToDelete = listOf<DocumentReference>()

    private lateinit var animalRef: DocumentReference

    private var birthsRetrieved = false
    private var breedingsRetrieved = false
    private var pedicuresRetrieved = false
    private var treatmentsRetrieved = false
    private var vaccinationsRetrieved = false
    private var disinfectionsRetrieved = false
    private var vitaminizationsRetrieved = false

    fun execute(animalId: String) {
        animalRef = farmReference.collection(FirestorePath.Collections.ANIMALS).document(animalId)

        val birthsReference = farmReference
            .collection(FirestorePath.Collections.BIRTHS)
            .whereEqualTo(FirestorePath.Birth.MOTHER_ID, animalId)

        val breedingsReference = farmReference
            .collection(FirestorePath.Collections.BREEDINGS)
            .whereEqualTo(FirestorePath.Breeding.FEMALE, animalId)

        val pedicuresReference = farmReference
            .collection(FirestorePath.Collections.PEDICURES)
            .whereEqualTo(FirestorePath.AnimalAction.ANIMAL_ID, animalId)

        val treatmentsReference = farmReference
            .collection(FirestorePath.Collections.TREATMENTS)
            .whereEqualTo(FirestorePath.AnimalAction.ANIMAL_ID, animalId)

        val vaccinationsReference = farmReference
            .collection(FirestorePath.Collections.VACCINATIONS)
            .whereEqualTo(FirestorePath.AnimalAction.ANIMAL_ID, animalId)

        val disinfectionsReference = farmReference
            .collection(FirestorePath.Collections.DISINFECTIONS)
            .whereEqualTo(FirestorePath.AnimalAction.ANIMAL_ID, animalId)

        val vitaminizationsReference = farmReference
            .collection(FirestorePath.Collections.VITAMINIZATIONS)
            .whereEqualTo(FirestorePath.AnimalAction.ANIMAL_ID, animalId)


        birthsReference.get()
            .addOnSuccessListener {
                birthsToDelete = it.documents.map { snapshot -> snapshot.reference }
            }
            .addOnFailureListener { error { it } }
            .addOnCompleteListener {
                birthsRetrieved = true
                checkRetrieveFinished()
            }

        breedingsReference.get()
            .addOnSuccessListener {
                breedingsToDelete = it.documents.map { snapshot -> snapshot.reference }
            }
            .addOnFailureListener { error { it } }
            .addOnCompleteListener {
                breedingsRetrieved = true
                checkRetrieveFinished()
            }

        pedicuresReference.get()
            .addOnSuccessListener {
                pedicuresToDelete = it.documents.map { snapshot -> snapshot.reference }
            }
            .addOnFailureListener { error { it } }
            .addOnCompleteListener {
                pedicuresRetrieved = true
                checkRetrieveFinished()
            }

        treatmentsReference.get()
            .addOnSuccessListener {
                treatmentsToDelete = it.documents.map { snapshot -> snapshot.reference }
            }
            .addOnFailureListener { error { it } }
            .addOnCompleteListener {
                treatmentsRetrieved = true
                checkRetrieveFinished()
            }

        vaccinationsReference.get()
            .addOnSuccessListener {
                vaccinationsToDelete = it.documents.map { snapshot -> snapshot.reference }
            }
            .addOnFailureListener { error { it } }
            .addOnCompleteListener {
                vaccinationsRetrieved = true
                checkRetrieveFinished()
            }

        disinfectionsReference.get()
            .addOnSuccessListener {
                disinfectionsToDelete = it.documents.map { snapshot -> snapshot.reference }
            }
            .addOnFailureListener { error { it } }
            .addOnCompleteListener {
                disinfectionsRetrieved = true
                checkRetrieveFinished()
            }

        vitaminizationsReference.get()
            .addOnSuccessListener {
                vitaminizationsToDelete = it.documents.map { snapshot -> snapshot.reference }
            }
            .addOnFailureListener { error { it } }
            .addOnCompleteListener {
                vitaminizationsRetrieved = true
                checkRetrieveFinished()
            }
    }

    private fun checkRetrieveFinished() {
        if (birthsRetrieved
            && breedingsRetrieved
            && pedicuresRetrieved
            && treatmentsRetrieved
            && vaccinationsRetrieved
            && disinfectionsRetrieved
            && vitaminizationsRetrieved
        ) {
            val batch = Firebase.firestore.batch()

            birthsToDelete.forEach { batch.delete(it) }
            breedingsToDelete.forEach { batch.delete(it) }
            pedicuresToDelete.forEach { batch.delete(it) }
            treatmentsToDelete.forEach { batch.delete(it) }
            vaccinationsToDelete.forEach { batch.delete(it) }
            disinfectionsToDelete.forEach { batch.delete(it) }
            vitaminizationsToDelete.forEach { batch.delete(it) }

            batch.delete(animalRef)

            batch.commit()
                .addOnSuccessListener { successListener() }
                .addOnFailureListener { failureListener(it) }
                .addOnCompleteListener { complete() }
        }
    }

}
