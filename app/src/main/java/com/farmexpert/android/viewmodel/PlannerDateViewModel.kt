/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/25/19 8:58 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farmexpert.android.utils.getNextDay
import com.farmexpert.android.utils.getPrevDay
import java.util.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, March 18, 2018.
 */
class PlannerDateViewModel : ViewModel() {

    private val date: MutableLiveData<Date> = MutableLiveData()

    fun setDate(newDate: Date) {
        date.value = newDate
    }

    fun prevDaySelected() {
        date.value?.run { this@PlannerDateViewModel.setDate(getPrevDay()) }
    }

    fun nextDaySelected() {
        date.value?.run { this@PlannerDateViewModel.setDate(getNextDay()) }
    }

    fun getDate(): LiveData<Date> = date

}