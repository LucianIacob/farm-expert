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
import androidx.annotation.DrawableRes
import com.farmexpert.android.R
import kotlinx.android.synthetic.main.layout_text_view_with_header.view.*

class TextViewWithHeaderAndExpandAndEdit(context: Context, attributes: AttributeSet) :
    LinearLayout(context, attributes) {

    private var isExpanded = false

    init {
        val typedArray =
            context.obtainStyledAttributes(
                attributes,
                R.styleable.TextViewWithHeaderAndExpandAndEdit,
                0,
                0
            )

        val header = typedArray.getString(R.styleable.TextViewWithHeaderAndExpandAndEdit_header)
        val value = typedArray.getString(R.styleable.TextViewWithHeaderAndExpandAndEdit_value)
        val expandable =
            typedArray.getBoolean(R.styleable.TextViewWithHeaderAndExpandAndEdit_expandable, false)

        typedArray.recycle()

        orientation = HORIZONTAL

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_text_view_with_header, this, true)

        text_with_header_title.text = header
        text_with_header_value.text = value
        text_with_header_expand.visibility = if (expandable) View.VISIBLE else View.GONE
    }

    fun setValue(value: String) {
        text_with_header_value.text = value
    }

    fun setExpandListener(viewToExpand: View) {
        text_with_header_expand.setOnClickListener {
            viewToExpand.visibility = if (isExpanded) GONE else VISIBLE
            setExpandIcon(if (isExpanded) R.drawable.baseline_expand_more_black_24 else R.drawable.baseline_expand_less_black_24)
            isExpanded = !isExpanded
        }
    }

    private fun setExpandIcon(@DrawableRes resId: Int) {
        text_with_header_expand.setImageResource(resId)
    }

    fun setEditListener(listener: OnClickListener) {
        text_with_header_edit.setOnClickListener(listener)
    }

}