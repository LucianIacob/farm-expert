/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/6/19 10:57 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.farmexpert.android.R
import com.farmexpert.android.adapter.holder.AnimalHolder
import com.farmexpert.android.model.Animal
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

open class AnimalsAdapter(
    options: FirestoreRecyclerOptions<Animal>,
    private val listener: (Animal) -> Unit,
    private val longClick: (Animal) -> Unit
) :
    FirestoreRecyclerAdapter<Animal, AnimalHolder>(options) {

    private var shouldListen: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_animal, parent, false)
        return AnimalHolder(view)
    }

    override fun onBindViewHolder(animalHolder: AnimalHolder, position: Int, animal: Animal) {
        animalHolder.bind(animal)
        animalHolder.itemView.setOnClickListener { listener(animal) }
        animalHolder.itemView.setOnLongClickListener {
            longClick(animal)
            true
        }
    }

    fun readyForListening() {
        shouldListen = true
        startListening()
    }

    override fun startListening() {
        if (shouldListen) {
            super.startListening()
        }
    }

}
