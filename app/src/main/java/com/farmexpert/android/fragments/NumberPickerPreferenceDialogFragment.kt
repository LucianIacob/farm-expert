/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/14/19 4:53 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.view.View
import android.widget.NumberPicker
import androidx.core.os.bundleOf
import androidx.preference.PreferenceDialogFragmentCompat
import com.farmexpert.android.R
import com.farmexpert.android.preference.NumberPickerPreference
import com.farmexpert.android.utils.ConfigPickerUtils

class NumberPickerPreferenceDialogFragment : PreferenceDialogFragmentCompat() {

    private lateinit var mNumberPicker: NumberPicker

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        isCancelable = false
        mNumberPicker = view.findViewById(R.id.number_picker)
        mNumberPicker.maxValue = ConfigPickerUtils.getMaxValueByKey(preference.key, resources)
        mNumberPicker.minValue = ConfigPickerUtils.getMinValueByKey(preference.key, resources)
        mNumberPicker.setFormatter { value -> resources.getQuantityString(R.plurals.days, value, value) }

        if (preference is NumberPickerPreference) {
            mNumberPicker.value = (preference as NumberPickerPreference).getValue()
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (!positiveResult) {
            return
        }
        if (preference is NumberPickerPreference) {
            if (preference.callChangeListener(mNumberPicker.value)) {
                (preference as NumberPickerPreference).setNumber(mNumberPicker.value)
            }
        }
    }

    companion object {
        fun newInstance(key: String): NumberPickerPreferenceDialogFragment {
            val fragmentCompat = NumberPickerPreferenceDialogFragment()
            fragmentCompat.arguments = bundleOf(ARG_KEY to key)
            return fragmentCompat
        }
    }
}
