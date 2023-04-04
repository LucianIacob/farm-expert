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
import androidx.core.view.isVisible
import com.farmexpert.android.model.Breeding
import com.farmexpert.android.utils.DropdownUtils
import com.farmexpert.android.utils.asDisplayable
import com.farmexpert.android.utils.takeIfNotBlank
import kotlinx.android.synthetic.main.item_breeding.view.*

class BreedingViewHolder(
    itemView: View,
    updateListener: (Breeding) -> Unit,
    deleteListener: (Breeding) -> Unit
) : BaseDetailHolder<Breeding>(itemView, updateListener, deleteListener) {

    override fun bind(entity: Breeding) {
        with(itemView) {
            date.text = entity.actionDate.asDisplayable()
            note.text = DropdownUtils.getBreedingNoteValue(entity.note, itemView.resources)
            male.text = entity.male
            estimatedBirth.text = entity.birthExpectedAt.asDisplayable()

            commentsContainer?.isVisible = entity.comments?.isNotBlank() ?: false
            entity.comments?.takeIfNotBlank()?.let {
                commentsDetails?.text = it
            }
            updateBtn.setOnClickListener { updateListener(entity) }
            deleteBtn.setOnClickListener { deleteListener(entity) }
        }
    }

}
