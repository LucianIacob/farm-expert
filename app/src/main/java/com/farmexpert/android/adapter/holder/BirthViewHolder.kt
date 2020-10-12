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
import com.farmexpert.android.model.Birth
import com.farmexpert.android.utils.*
import kotlinx.android.synthetic.main.item_birth.view.*

class BirthViewHolder(
    itemView: View,
    updateListener: (Birth) -> Unit,
    deleteListener: (Birth) -> Unit
) : BaseDetailHolder<Birth>(itemView, updateListener, deleteListener) {

    override fun bind(entity: Birth) {
        with(itemView) {
            birthDate.text = entity.dateOfBirth.asDisplayable()
            note.text = DropdownUtils.getBirthNoteValue(entity.note, itemView.resources)
            calfId.text = entity.calfId
            entity.comments?.takeIfNotBlank()?.let {
                commentsContainer?.visible()
                commentsDetails?.text = it
            } ?: run { commentsContainer?.gone() }
            updateBtn.setOnClickListener { updateListener(entity) }
            deleteBtn.setOnClickListener { deleteListener(entity) }
        }
    }

}
