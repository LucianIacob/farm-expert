/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 7/13/19 11:19 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.utils

import com.google.firebase.Timestamp
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*

class AppUtilsTest {

    @Test
    fun testTimestampCompare() {
        val earlyTimestamp = Timestamp(Date().shift(-1))
        val futureTimestamp = Timestamp(Date().shift(0, TimeOfTheDay.END))
        val currentTimestamp = Timestamp.now()

        assertTrue(earlyTimestamp.compareTo(currentTimestamp) == -1)
        assertTrue(currentTimestamp.compareTo(earlyTimestamp) == 1)
        assertTrue(futureTimestamp.compareTo(earlyTimestamp) == 1)
        assertTrue(futureTimestamp.compareTo(currentTimestamp) == 1)
        assertTrue(currentTimestamp.compareTo(currentTimestamp) == 0)
    }

}