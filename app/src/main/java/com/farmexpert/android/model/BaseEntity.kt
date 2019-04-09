/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/9/19 9:25 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.model

import com.google.firebase.firestore.Exclude

open class BaseEntity {
    @Exclude
    var id: String? = null
        @Exclude get() {
            return field
        }
        @Exclude set(value) {
            field = value
        }
}