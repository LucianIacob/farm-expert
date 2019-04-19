/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/19/19 9:11 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter.holder

import android.view.View
import android.widget.TextView
import com.farmexpert.android.model.Breeding
import com.farmexpert.android.utils.asDisplayable
import com.farmexpert.android.utils.day
import com.farmexpert.android.utils.month
import kotlinx.android.synthetic.main.item_graph_breeding.view.*
import java.util.*

class GraphBreedingViewHolder(val view: View) : BaseMasterHolder<Breeding>(view) {

    override fun bind(key: String, values: List<Breeding>): Unit = with(view) {
        femaleCell.text = key
        values.maxBy { it.actionDate }?.let {
            maleCell.text = it.male
            estimatedBirth.text = it.birthExpectedAt.asDisplayable()
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
