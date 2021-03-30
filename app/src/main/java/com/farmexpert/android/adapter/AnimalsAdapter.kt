/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/9/19 9:25 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter

import android.view.ViewGroup
import com.farmexpert.android.R
import com.farmexpert.android.adapter.holder.AnimalHolder
import com.farmexpert.android.model.Animal
import com.farmexpert.android.utils.inflate
import com.firebase.ui.firestore.FirestoreRecyclerOptions

open class AnimalsAdapter(
    options: FirestoreRecyclerOptions<Animal>,
    private val clickListener: (Animal) -> Unit,
    private val longClickListener: (String) -> Unit
) : BaseFirestoreRecyclerAdapter<Animal, AnimalHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AnimalHolder(parent.inflate(R.layout.item_animal))

    override fun onBindViewHolder(animalHolder: AnimalHolder, position: Int, animal: Animal) {
        animalHolder.bind(animal)
        animalHolder.itemView.setOnClickListener { clickListener(animal) }
        animalHolder.itemView.setOnLongClickListener {
            animal.id?.let { longClickListener.invoke(it) }
            true
        }
    }
}
