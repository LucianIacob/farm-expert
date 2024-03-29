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
import android.widget.TextView
import com.farmexpert.android.R
import com.farmexpert.android.model.AnimalAction
import com.farmexpert.android.utils.day
import com.farmexpert.android.utils.month
import kotlinx.android.synthetic.main.item_graph_animal_action.view.*
import java.util.*

class GraphAnimalActionHolder(
    val view: View,
    val animalIdClick: (String) -> Unit
) : BaseMasterHolder<AnimalAction>(itemView = view) {

    override fun bind(key: String, values: List<AnimalAction>) = with(view) {
        val digitsToShow = resources.getInteger(R.integer.animal_id_digits_to_show)

        animalCell.text = key.takeLast(digitsToShow)
        animalCell.setOnClickListener { animalIdClick(key) }

        val graphViews: Map<Int, TextView> = mapOf(
            Calendar.JANUARY to janCell,
            Calendar.FEBRUARY to febCell,
            Calendar.MARCH to marCell,
            Calendar.APRIL to aprCell,
            Calendar.MAY to mayCell,
            Calendar.JUNE to junCell,
            Calendar.JULY to julCell,
            Calendar.AUGUST to augCell,
            Calendar.SEPTEMBER to sepCell,
            Calendar.OCTOBER to octCell,
            Calendar.NOVEMBER to novCell,
            Calendar.DECEMBER to decCell
        )

        graphViews.values.forEach { it.text = "" }

        values.forEach {
            val month = it.actionDate.month()
            val sb = StringBuffer(graphViews.getValue(month).text)

            with(sb) {
                if (isNotEmpty()) {
                    append(System.lineSeparator())
                }
                append(it.actionDate.day())
            }

            graphViews.getValue(month).text = sb.toString()
        }
    }
}
