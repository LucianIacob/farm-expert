/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/18/19 9:35 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter.holder

import android.view.View
import android.widget.TextView
import com.farmexpert.android.model.Birth
import kotlinx.android.synthetic.main.item_graph_birth.view.*
import java.util.*

class GraphBirthViewHolder(itemView: View) : BaseMasterHolder<Birth>(itemView) {

    override fun bind(entity: Birth) = with(itemView) {
        motherCell.text = entity.motherId
        calfCell.text = entity.calfId

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

        val dateToDisplay = entity.dateOfBirth.toDate()
        val calendar = Calendar.getInstance().apply { time = dateToDisplay }

        val monthToPopulate = calendar.get(Calendar.MONTH)

        graphViews[monthToPopulate]?.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
    }

}
