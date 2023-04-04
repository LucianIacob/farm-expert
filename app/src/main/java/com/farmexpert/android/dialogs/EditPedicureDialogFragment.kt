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
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import com.farmexpert.android.R
import com.farmexpert.android.utils.AppUtils
import kotlinx.android.synthetic.main.dialog_edit_pedicure.view.*
import java.util.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, March 16, 2018.
 */
class EditPedicureDialogFragment : BaseEditRecordDialogFragment(R.layout.dialog_edit_pedicure) {

    override var titleRes = R.string.edit_pedicure_title

    private lateinit var mNailsMap: HashMap<View, Int>

    override fun setupDetails() {
        mView?.dialogDetails?.setText(details?.drop(8))
    }

    override fun onUiElementsReady() {
        super.onUiElementsReady()

        mView?.run {
            mNailsMap = hashMapOf(
                leftTopLeftNail to AppUtils.populateLeftNail(
                    leftTopLeftNail,
                    details?.substring(0, 1)
                ),
                leftTopRightNail to AppUtils.populateRightNail(
                    leftTopRightNail,
                    details?.substring(1, 2)
                ),
                rightTopLeftNail to AppUtils.populateLeftNail(
                    rightTopLeftNail,
                    details?.substring(2, 3)
                ),
                rightTopRightNail to AppUtils.populateRightNail(
                    rightTopRightNail,
                    details?.substring(3, 4)
                ),
                leftBottomLeftNail to AppUtils.populateLeftNail(
                    leftBottomLeftNail,
                    details?.substring(4, 5)
                ),
                leftBottomRightNail to AppUtils.populateRightNail(
                    leftBottomRightNail,
                    details?.substring(5, 6)
                ),
                rightBottomLeftNail to AppUtils.populateLeftNail(
                    rightBottomLeftNail,
                    details?.substring(6, 7)
                ),
                rightBottomRightNail to AppUtils.populateRightNail(
                    rightBottomRightNail,
                    details?.substring(7, 8)
                )
            )
        }
        setupHoofs()
    }

    override fun sendNewRecord() {
        val bundle = bundleOf(
            EDIT_DIALOG_DOC_ID to documentId,
            DIALOG_DATE to currentDate.time,
            DIALOG_DETAILS to getEncodedHoofs() + mView?.dialogDetails?.text.toString()
        )
        val intent = Intent().putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            DIALOG_DETAILS,
            getEncodedHoofs() + mView?.dialogDetails?.text.toString()
        )
    }

    private fun setupHoofs() {
        mView?.run {
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