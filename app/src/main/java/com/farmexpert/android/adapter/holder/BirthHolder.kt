/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/10/19 9:18 AM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.adapter.holder

import android.view.View
import com.farmexpert.android.model.Birth
import com.farmexpert.android.utils.asDisplayable
import kotlinx.android.synthetic.main.item_birth.view.*

class BirthHolder(itemView: View) : BaseDetailHolder<Birth>(itemView) {

    override fun bind(entity: Birth) {
        with(itemView) {
            birthDate.text = entity.dateOfBirth.asDisplayable()
            note.text = entity.note
            calfId.text = entity.calfId
        }
    }

}
