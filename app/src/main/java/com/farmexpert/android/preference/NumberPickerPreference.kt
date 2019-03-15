/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/14/19 4:46 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.preference

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.preference.DialogPreference
import com.farmexpert.android.R

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, October 17, 2018.
 */
class NumberPickerPreference(context: Context, attrs: AttributeSet) :
    DialogPreference(context, attrs, R.attr.dialogPreferenceStyle) {

    init {
        dialogLayoutResource = R.layout.dialog_number_picker
        positiveButtonText = "Ok"
        negativeButtonText = "Cancel"
        dialogIcon = null
        isIconSpaceReserved = false
    }

    private var mValue: Int = 1

    override fun onGetDefaultValue(a: TypedArray?, index: Int): Any {
        return a!!.getInt(index, mValue)
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        defaultValue?.let {
            setNumber(getPersistedInt(it.toString().toInt()))
        } ?: run {
            setNumber(sharedPreferences.getInt(key, mValue))
        }
    }

    fun setNumber(value: Int) {
        mValue = value
        persistInt(value)
    }

    fun getValue() = mValue
}