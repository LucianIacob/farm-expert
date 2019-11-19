/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/10/19 9:18 AM.
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
        val digitsToShow = resources.getInteger(R.integer.graph_key_take_digits)

        itemAnimalId.text = animal.id?.takeLast(digitsToShow)
        itemAnimalDate.text = animal.dateOfBirth.asDisplayable()
    }

}
