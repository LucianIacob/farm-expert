/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/23/19 10:30 AM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.model.Animal
import com.farmexpert.android.utils.getShort
import kotlinx.android.synthetic.main.item_animal.view.*

class AnimalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(animal: Animal) {
        itemView.itemAnimalId.text = animal.id
        itemView.itemAnimalDate.text = animal.dateOfBirth.toDate().getShort()
    }

}
