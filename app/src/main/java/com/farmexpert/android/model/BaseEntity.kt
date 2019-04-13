/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/13/19 9:17 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.model

import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.firestore.Exclude

open class BaseEntity {

    open fun getEditDialogArgs(): Bundle = bundleOf()

    @Exclude
    var id: String? = null
        @Exclude get() {
            return field
        }
        @Exclude set(value) {
            field = value
        }
}