/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.R
import com.farmexpert.android.model.Animal
import com.farmexpert.android.utils.asDisplayable
import kotlinx.android.synthetic.main.item_animal.view.*

class AnimalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(animal: Animal) = with(itemView) {
        val digitsToShow = resources.getInteger(R.integer.animal_id_digits_to_show)

        itemAnimalId.text = animal.id?.takeLast(digitsToShow)
        itemAnimalDate.text = animal.dateOfBirth.asDisplayable()
    }

}
