/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 7/17/19 10:02 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.model

import com.farmexpert.android.utils.FirestorePath
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.util.*

data class Reminder(
    @PropertyName(FirestorePath.Reminder.REMINDER_DATE) val reminderDate: Timestamp = Timestamp(Date()),
    @PropertyName(FirestorePath.Reminder.DETAILS) val details: String = "",
    @PropertyName(FirestorePath.Reminder.CREATED_BY) val createdBy: String? = "",
    @PropertyName(FirestorePath.Reminder.HOLDER_PARENT) val holderParent: String
) : BaseEntity()