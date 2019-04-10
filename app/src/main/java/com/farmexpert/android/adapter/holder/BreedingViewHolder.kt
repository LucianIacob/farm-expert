/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/10/19 9:16 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter.holder

import android.view.View
import com.farmexpert.android.model.Breeding
import com.farmexpert.android.utils.asDisplayable
import kotlinx.android.synthetic.main.item_breeding.view.*

class BreedingViewHolder(
    itemView: View,
    updateListener: (Breeding) -> Unit,
    deleteListener: (Breeding) -> Unit
) : BaseDetailHolder<Breeding>(itemView, updateListener, deleteListener) {

    override fun bind(entity: Breeding) {
        with(itemView) {
            date.text = entity.actionDate.asDisplayable()
            note.text = entity.note
            male.text = entity.male
            estimatedBirth.text = entity.birthExpectedAt.asDisplayable()
            updateBtn.setOnClickListener { updateListener(entity) }
            deleteBtn.setOnClickListener { deleteListener(entity) }
        }
    }

}
