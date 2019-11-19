/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/20/19 3:43 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter.holder

import android.view.View
import android.widget.TextView
import com.farmexpert.android.R
import com.farmexpert.android.model.Birth
import com.farmexpert.android.utils.day
import com.farmexpert.android.utils.month
import kotlinx.android.synthetic.main.item_graph_birth.view.*
import java.util.*

class GraphBirthViewHolder(
    itemView: View,
    val motherIdClick: (String) -> Unit,
    val calfIdClick: (String) -> Unit
) : BaseMasterHolder<Birth>(itemView = itemView) {

    override fun bind(key: String, values: List<Birth>) = with(itemView) {
        val digitsToShow = resources.getInteger(R.integer.animal_id_digits_to_show)

        with(motherCell) {
            text = key.takeLast(digitsToShow)
            setOnClickListener { motherIdClick(key) }
        }

        values.maxBy { it.dateOfBirth }?.calfId?.let { calfId ->
            calfCell.text = calfId.takeLast(digitsToShow)
            calfCell.setOnClickListener { calfIdClick(calfId) }
        }

        val graphViews: Map<Int, TextView> = hashMapOf(
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
            val month = it.dateOfBirth.month()
            val sb = StringBuffer(graphViews.getValue(month).text)

            with(sb) {
                if (isNotEmpty()) {
                    append(System.lineSeparator())
                }
                append(it.dateOfBirth.day())
            }

            graphViews.getValue(month).text = sb.toString()
        }
    }
}