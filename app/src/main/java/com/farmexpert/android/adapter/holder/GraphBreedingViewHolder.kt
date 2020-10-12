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
import com.farmexpert.android.model.Breeding
import com.farmexpert.android.utils.asDisplayable
import com.farmexpert.android.utils.day
import com.farmexpert.android.utils.month
import kotlinx.android.synthetic.main.item_graph_breeding.view.*
import java.util.*

class GraphBreedingViewHolder(
    val view: View,
    val femaleIdClick: (String) -> Unit,
    val maleIdClick: (String) -> Unit
) : BaseMasterHolder<Breeding>(view) {

    override fun bind(key: String, values: List<Breeding>): Unit = with(view) {
        with(femaleCell) {
            val digitsToShow = resources.getInteger(R.integer.animal_id_digits_to_show)
            text = key.takeLast(digitsToShow)
            setOnClickListener { femaleIdClick(key) }
        }
        values.maxByOrNull { it.actionDate }?.let { breeding ->
            maleCell.text = breeding.male
            estimatedBirth.text = breeding.birthExpectedAt.asDisplayable()
            maleCell.setOnClickListener { maleIdClick(breeding.male) }
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