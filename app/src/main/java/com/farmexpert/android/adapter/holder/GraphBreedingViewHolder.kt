/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/18/19 10:14 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter.holder

import android.view.View
import android.widget.TextView
import com.farmexpert.android.model.Breeding
import com.farmexpert.android.utils.asDisplayable
import kotlinx.android.synthetic.main.item_graph_breeding.view.*
import java.util.*

class GraphBreedingViewHolder(val view: View) : BaseMasterHolder<Breeding>(view) {

    override fun bind(entity: Breeding) {
        with(view) {
            femaleCell.text = entity.female
            maleCell.text = entity.male
            estimatedBirth.text = entity.birthExpectedAt.asDisplayable()

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

            val dateToDisplay = entity.actionDate.toDate()
            val calendar = Calendar.getInstance().apply { time = dateToDisplay }

            val monthToPopulate = calendar.get(Calendar.MONTH)

            graphViews[monthToPopulate]?.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
        }
    }

}
