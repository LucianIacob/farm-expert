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
import com.farmexpert.android.model.Birth
import com.farmexpert.android.utils.asDisplayable
import kotlinx.android.synthetic.main.item_birth.view.*

class BirthViewHolder(
    itemView: View,
    updateListener: (Birth) -> Unit,
    deleteListener: (Birth) -> Unit
) : BaseDetailHolder<Birth>(itemView, updateListener, deleteListener) {

    override fun bind(entity: Birth) {
        with(itemView) {
            birthDate.text = entity.dateOfBirth.asDisplayable()
            note.text = entity.note
            calfId.text = entity.calfId
            updateBtn.setOnClickListener { updateListener(entity) }
            deleteBtn.setOnClickListener { deleteListener(entity) }
        }
    }

}
