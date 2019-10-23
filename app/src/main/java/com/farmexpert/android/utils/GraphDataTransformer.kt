/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/19/19 9:11 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

import com.farmexpert.android.model.AnimalAction
import com.farmexpert.android.model.Birth
import com.farmexpert.android.model.Breeding
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject

class GraphDataTransformer {

    companion object {

        fun transformDocumentsForBirthsGraph(documents: QuerySnapshot?): Map<String, List<Birth>> {
            val adapterMap = mutableMapOf<String, MutableList<Birth>>()

            documents?.forEach { document ->
                val birth = document.toObject<Birth>()
                if (!adapterMap.containsKey(birth.motherId)) {
                    adapterMap[birth.motherId] = mutableListOf()
                }

                adapterMap[birth.motherId]?.add(birth)
            }

            return adapterMap
        }

        fun transformDocumentsForAnimalActionsGraph(documents: QuerySnapshot?): MutableMap<String, MutableList<AnimalAction>> {
            val adapterMap = mutableMapOf<String, MutableList<AnimalAction>>()

            documents?.forEach { document ->
                val animalAction = document.toObject<AnimalAction>()
                if (!adapterMap.containsKey(animalAction.animalId)) {
                    adapterMap[animalAction.animalId] = mutableListOf()
                }

                adapterMap[animalAction.animalId]?.add(animalAction)
            }

            return adapterMap
        }

        fun transformDocumentsForBreedingsGraph(documents: QuerySnapshot?): Map<String, List<Breeding>> {
            val adapterMap = mutableMapOf<String, MutableList<Breeding>>()

            documents?.forEach { document ->
                val breeding = document.toObject<Breeding>()
                if (!adapterMap.containsKey(breeding.female)) {
                    adapterMap[breeding.female] = mutableListOf()
                }

                adapterMap[breeding.female]?.add(breeding)
            }

            return adapterMap
        }

    }

}
