/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:13 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import com.farmexpert.android.R
import com.farmexpert.android.utils.AppUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_add_pedicure.view.*

class AddPedicureDialogFragment : BaseAddRecordDialogFragment() {

    private lateinit var mNailsMap: HashMap<View, Int>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = View.inflate(activity, R.layout.dialog_add_pedicure, null)
        onUiElementsReady()

        context?.let { context ->
            return MaterialAlertDialogBuilder(context)
                .setView(mView)
                .setTitle(R.string.add_pedicure_title)
                .setPositiveButton(R.string.dialog_add_positive_btn) { _, _ -> addRecord() }
                .setNegativeButton(R.string.dialog_cancel_btn, null)
                .setCancelable(false)
                .create()
        } ?: throw IllegalArgumentException()
    }

    override fun onUiElementsReady() {
        super.onUiElementsReady()
        mView?.run {

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
                mNailsMap[nail] =
                    if (mNailsMap[nail] == leftNailDefault) leftNailProblem
                    else leftNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }

            leftTopRightNail.setOnClickListener { nail ->
                mNailsMap[nail] =
                    if (mNailsMap[nail] == rightNailDefault) rightNailProblem
                    else rightNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }

            rightTopLeftNail.setOnClickListener { nail ->
                mNailsMap[nail] =
                    if (mNailsMap[nail] == leftNailDefault) leftNailProblem
                    else leftNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }

            rightTopRightNail.setOnClickListener { nail ->
                mNailsMap[nail] =
                    if (mNailsMap[nail] == rightNailDefault) rightNailProblem
                    else rightNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }

            leftBottomLeftNail.setOnClickListener { nail ->
                mNailsMap[nail] =
                    if (mNailsMap[nail] == leftNailDefault) leftNailProblem
                    else leftNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }

            leftBottomRightNail.setOnClickListener { nail ->
                mNailsMap[nail] =
                    if (mNailsMap[nail] == rightNailDefault) rightNailProblem
                    else rightNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }

            rightBottomLeftNail.setOnClickListener { nail ->
                mNailsMap[nail] =
                    if (mNailsMap[nail] == leftNailDefault) leftNailProblem
                    else leftNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }

            rightBottomRightNail.setOnClickListener { nail ->
                mNailsMap[nail] =
                    if (mNailsMap[nail] == rightNailDefault) rightNailProblem
                    else rightNailDefault

                mNailsMap[nail]?.let { (nail as? ImageView)?.setImageResource(it) }
            }
        }
    }

    private fun addRecord() {
        val bundle = bundleOf(
            ADD_DIALOG_DETAILS to getEncodedHoofs() + mView?.dialogDetails?.text.toString(),
            DIALOG_DATE to currentDate.time
        )
        val intent = Intent().putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    private fun getEncodedHoofs(): String {
        val stringBuilder = StringBuilder()
        mView?.run {
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

fun Int.encode(): String =
    if (this == R.drawable.left_nail_problem || this == R.drawable.right_nail_problem) AppUtils.NAIL_WITH_PROBLEM
    else AppUtils.NAIL_WITHOUT_PROBLEM
