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
import androidx.core.view.isVisible
import com.farmexpert.android.model.Birth
import com.farmexpert.android.utils.DropdownUtils
import com.farmexpert.android.utils.asDisplayable
import com.farmexpert.android.utils.takeIfNotBlank
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
            commentsContainer?.isVisible = entity.comments?.isNotBlank() ?: false
            entity.comments?.takeIfNotBlank()?.let {
                commentsDetails?.text = it
            }
            updateBtn.setOnClickListener { updateListener(entity) }
            deleteBtn.setOnClickListener { deleteListener(entity) }
        }
    }

}
