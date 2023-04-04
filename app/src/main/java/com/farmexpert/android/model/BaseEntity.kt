/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.model

import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.firestore.Exclude

open class BaseEntity {

    @Exclude
    open fun getEditDialogArgs(): Bundle = bundleOf()

    @Exclude
    var id: String? = null
        @Exclude get
        @Exclude set
}