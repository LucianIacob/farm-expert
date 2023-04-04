/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
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
