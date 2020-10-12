package com.farmexpert.android.dialogs

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.farmexpert.android.R
import com.farmexpert.android.utils.getShort
import com.farmexpert.android.utils.takeIfExists
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.util.*

abstract class BaseDialogFragment : DialogFragment() {

    protected var mView: View? = null

    private val dateTextView: TextView? by lazy { mView?.findViewById(R.id.dialogDate) }

    private val detailsTextView: TextView? by lazy { mView?.findViewById(R.id.dialogDetails) }

    internal var currentDate: Date = Date()

    internal var details: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentDate = savedInstanceState?.getLong(DIALOG_DATE)
            ?.takeIfExists()
            ?.let { Date(it) }
            ?: run {
                arguments?.getLong(DIALOG_DATE)
                    ?.takeIfExists()
                    ?.let { Date(it) }
                    ?: Date()
            }

        details = savedInstanceState?.getString(DIALOG_DETAILS)
            ?: arguments?.getString(DIALOG_DETAILS)
    }

    open fun onUiElementsReady() {
        dateTextView?.setOnClickListener { onChangeDateClick() }
        setupDetails()
        setupDate()
    }

    open fun setupDetails() {
        detailsTextView?.text = details
    }

    fun fillDropdownComponent(
        textView: MaterialAutoCompleteTextView?,
        stringArray: Int,
        selected: Int? = null
    ) {
        textView?.let {
            val values = resources.getStringArray(stringArray)
            textView.setText(selected?.let { values[selected] } ?: values[0])

            val adapter = ArrayAdapter(
                textView.context,
                R.layout.spinner_item_layout,
                values
            )

            textView.setAdapter(adapter)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(DIALOG_DETAILS, details)
        outState.putLong(DIALOG_DATE, currentDate.time)
    }

    private fun setupDate() {
        dateTextView?.text = currentDate.getShort()
    }

    private fun onChangeDateClick() {
        MaterialDatePicker.Builder
            .datePicker()
            .setSelection(currentDate.time)
            .build()
            .apply {
                isCancelable = false
                addOnPositiveButtonClickListener {
                    currentDate = Date(it)
                    setupDate()
                }
                showNow(this@BaseDialogFragment.parentFragmentManager, "DatePicker")
            }
    }

    companion object {
        const val DIALOG_DATE = "com.farmexpert.android.Date"
        const val DIALOG_DETAILS = "com.farmexpert.android.Details"
    }
}