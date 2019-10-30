/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/13/19 9:05 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import com.farmexpert.android.R
import com.farmexpert.android.utils.applyFarmexpertStyle
import com.farmexpert.android.utils.encode
import kotlinx.android.synthetic.main.dialog_add_pedicure.view.*

class AddPedicureDialogFragment : BaseAddRecordDialogFragment() {

    private lateinit var mNailsMap: HashMap<View, Int>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = View.inflate(activity, R.layout.dialog_add_pedicure, null)
        mView.dialogDate.setOnClickListener { onChangeDateClick() }
        setupHoofs()
        setupDate()

        context?.let { context ->
            return AlertDialog.Builder(context, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                .setView(mView)
                .setTitle(R.string.add_pedicure_title)
                .setPositiveButton(R.string.dialog_add_positive_btn) { _, _ -> addRecord() }
                .setNegativeButton(R.string.dialog_cancel_btn, null)
                .setCancelable(false)
                .create()
                .apply {
                    setOnShowListener {
                        getButton(BUTTON_NEGATIVE).applyFarmexpertStyle(context)
                        getButton(BUTTON_POSITIVE).applyFarmexpertStyle(context)
                    }
                }
        } ?: throw IllegalArgumentException()
    }

    override fun getDateView(): TextView = mView.dialogDate

    private fun setupHoofs() {
        with(mView) {

            mNailsMap = hashMapOf(
                leftTopLeftNail to R.drawable.left_nail_default,
                leftTopRightNail to R.drawable.right_nail_default,
                rightTopLeftNail to R.drawable.left_nail_default,
                rightTopRightNail to R.drawable.right_nail_default,
                leftBottomLeftNail to R.drawable.left_nail_default,
                leftBottomRightNail to R.drawable.right_nail_default,
                rightBottomLeftNail to R.drawable.left_nail_default,
                rightBottomRightNail to R.drawable.right_nail_default
            )

            leftTopLeftNail.setOnClickListener { nail ->
                mNailsMap[nail] = if (mNailsMap[nail] == leftNailDefault) {
                    leftNailProblem
                } else leftNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }

            leftTopRightNail.setOnClickListener { nail ->
                mNailsMap[nail] = if (mNailsMap[nail] == rightNailDefault) {
                    rightNailProblem
                } else rightNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }

            rightTopLeftNail.setOnClickListener { nail ->
                mNailsMap[nail] = if (mNailsMap[nail] == leftNailDefault) {
                    leftNailProblem
                } else leftNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }

            rightTopRightNail.setOnClickListener { nail ->
                mNailsMap[nail] = if (mNailsMap[nail] == rightNailDefault) {
                    rightNailProblem
                } else rightNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }

            leftBottomLeftNail.setOnClickListener { nail ->
                mNailsMap[nail] = if (mNailsMap[nail] == leftNailDefault) {
                    leftNailProblem
                } else leftNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }

            leftBottomRightNail.setOnClickListener { nail ->
                mNailsMap[nail] = if (mNailsMap[nail] == rightNailDefault) {
                    rightNailProblem
                } else rightNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }

            rightBottomLeftNail.setOnClickListener { nail ->
                mNailsMap[nail] = if (mNailsMap[nail] == leftNailDefault) {
                    leftNailProblem
                } else leftNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }

            rightBottomRightNail.setOnClickListener { nail ->
                mNailsMap[nail] = if (mNailsMap[nail] == rightNailDefault) {
                    rightNailProblem
                } else rightNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }
        }
    }

    private fun addRecord() {
        val bundle = bundleOf(
            ADD_DIALOG_DETAILS to getEncodedHoofs() + mView.details.text.toString(),
            ADD_DIALOG_DATE to mSetDate.time
        )
        val intent = Intent().putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    private fun getEncodedHoofs(): String {
        val stringBuilder = StringBuilder()
        with(mView) {
            stringBuilder
                .append(mNailsMap[leftTopLeftNail]?.encode())
                .append(mNailsMap[leftTopRightNail]?.encode())
                .append(mNailsMap[rightTopLeftNail]?.encode())
                .append(mNailsMap[rightTopRightNail]?.encode())
                .append(mNailsMap[leftBottomLeftNail]?.encode())
                .append(mNailsMap[leftBottomRightNail]?.encode())
                .append(mNailsMap[rightBottomLeftNail]?.encode())
                .append(mNailsMap[rightBottomRightNail]?.encode())
        }
        return stringBuilder.toString()
    }

    companion object {
        const val leftNailDefault = R.drawable.left_nail_default
        const val leftNailProblem = R.drawable.left_nail_problem
        const val rightNailDefault = R.drawable.right_nail_default
        const val rightNailProblem = R.drawable.right_nail_problem
    }
}