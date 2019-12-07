/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 10/21/19 5:25 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.farmexpert.android.R
import kotlinx.android.synthetic.main.layout_text_view_with_header.view.*

class TextViewWithHeaderAndExpandAndEdit(
    context: Context,
    attributes: AttributeSet
) : LinearLayout(context, attributes) {

    private var isExpanded = false

    var value: String
        get() = text_with_header_value.text.toString()
        set(value) {
            text_with_header_value.text = value
        }

    init {
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater)
            ?.inflate(R.layout.layout_text_view_with_header, this, true)
        orientation = HORIZONTAL

        context.obtainStyledAttributes(
            attributes,
            R.styleable.TextViewWithHeaderAndExpandAndEdit,
            0,
            0
        ).run {
            text_with_header_title.text =
                getString(R.styleable.TextViewWithHeaderAndExpandAndEdit_header)
            getString(R.styleable.TextViewWithHeaderAndExpandAndEdit_value)?.let {
                this@TextViewWithHeaderAndExpandAndEdit.value = it
            }
            text_with_header_expand.visibility =
                if (getBoolean(R.styleable.TextViewWithHeaderAndExpandAndEdit_expandable, false))
                    View.VISIBLE
                else View.GONE

            recycle()
        }
    }

    fun setExpandListener(viewToExpand: View) {
        text_with_header_expand.setOnClickListener {
            viewToExpand.visibility = if (isExpanded) GONE else VISIBLE
            text_with_header_expand.setImageResource(
                if (isExpanded) R.drawable.baseline_expand_more_black_24
                else R.drawable.baseline_expand_less_black_24
            )
            isExpanded = !isExpanded
        }
    }

    fun setEditListener(listener: OnClickListener) {
        text_with_header_edit.setOnClickListener(listener)
    }
}