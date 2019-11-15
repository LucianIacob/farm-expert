/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 10/23/19 12:26 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter.holder

import android.view.View
import com.farmexpert.android.model.Breeding
import com.farmexpert.android.utils.*
import kotlinx.android.synthetic.main.item_breeding.view.*

class BreedingViewHolder(
    itemView: View,
    updateListener: (Breeding) -> Unit,
    deleteListener: (Breeding) -> Unit
) : BaseDetailHolder<Breeding>(itemView, updateListener, deleteListener) {

    override fun bind(entity: Breeding) {
        with(itemView) {
            date.text = entity.actionDate.asDisplayable()
            note.text = SpinnerUtils.getBreedingNoteValue(entity.note, itemView.resources)
            male.text = entity.male
            estimatedBirth.text = entity.birthExpectedAt.asDisplayable()
            entity.comments?.takeIfNotBlank()?.let {
                commentsContainer?.visible()
                commentsDetails?.text = it
            } ?: run { commentsContainer?.gone() }
            updateBtn.setOnClickListener { updateListener(entity) }
            deleteBtn.setOnClickListener { deleteListener(entity) }
        }
    }

}
