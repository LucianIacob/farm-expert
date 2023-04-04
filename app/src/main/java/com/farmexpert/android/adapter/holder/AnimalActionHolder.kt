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
import com.farmexpert.android.model.AnimalAction
import com.farmexpert.android.utils.asDisplayable
import kotlinx.android.synthetic.main.item_animal_action.view.*

class AnimalActionHolder(
    itemView: View,
    updateListener: (AnimalAction) -> Unit,
    deleteListener: (AnimalAction) -> Unit
) : BaseDetailHolder<AnimalAction>(itemView, updateListener, deleteListener) {

    override fun bind(entity: AnimalAction) =
        with(itemView) {
            item_animal_action_date.text = entity.actionDate.asDisplayable()
            item_animal_action_detail.text = entity.details
            updateBtn.setOnClickListener { updateListener(entity) }
            deleteBtn.setOnClickListener { deleteListener(entity) }
        }
}
