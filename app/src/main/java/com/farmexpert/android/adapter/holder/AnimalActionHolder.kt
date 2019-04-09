/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/9/19 9:25 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter.holder

import android.view.View
import android.widget.TextView
import com.farmexpert.android.R
import com.farmexpert.android.model.AnimalAction

class AnimalActionHolder(itemView: View) : BaseDetailHolder<AnimalAction>(itemView) {

    override fun bind(entity: AnimalAction) {
        itemView.findViewById<TextView>(R.id.item_animal_action_detail).text = entity.details
    }

}
