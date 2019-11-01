/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/15/19 1:08 PM.
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
import com.farmexpert.android.utils.encode
import kotlinx.android.synthetic.main.dialog_edit_pedicure.view.*
import java.util.*

/**
 * Created by Lucian Iacob.
 * Cluj-Napoca, March 16, 2018.
 */
class EditPedicureDialogFragment : BaseEditRecordDialogFragment() {

    private var mDefaultDetails: String? = null
    private lateinit var mNailsMap: HashMap<View, Int>

    override fun extractAdditionalArgs(savedInstanceState: Bundle?, bundle: Bundle) {
        mDefaultDetails = savedInstanceState?.getString(EDIT_DIALOG_DETAILS)?.let { it }
            ?: run { bundle.getString(EDIT_DIALOG_DETAILS) }
    }

    override fun getTitle() = R.string.edit_pedicure_title

    override fun populateFields() {
        mView?.details?.setText(mDefaultDetails?.drop(8))

        mView?.run {
            mNailsMap = hashMapOf(
                leftTopLeftNail to AppUtils.populateLeftNail(
                    leftTopLeftNail,
                    mDefaultDetails?.substring(0, 1)
                ),
                leftTopRightNail to AppUtils.populateRightNail(
                    leftTopRightNail,
                    mDefaultDetails?.substring(1, 2)
                ),
                rightTopLeftNail to AppUtils.populateLeftNail(
                    rightTopLeftNail,
                    mDefaultDetails?.substring(2, 3)
                ),
                rightTopRightNail to AppUtils.populateRightNail(
                    rightTopRightNail,
                    mDefaultDetails?.substring(3, 4)
                ),
                leftBottomLeftNail to AppUtils.populateLeftNail(
                    leftBottomLeftNail,
                    mDefaultDetails?.substring(4, 5)
                ),
                leftBottomRightNail to AppUtils.populateRightNail(
                    leftBottomRightNail,
                    mDefaultDetails?.substring(5, 6)
                ),
                rightBottomLeftNail to AppUtils.populateLeftNail(
                    rightBottomLeftNail,
                    mDefaultDetails?.substring(6, 7)
                ),
                rightBottomRightNail to AppUtils.populateRightNail(
                    rightBottomRightNail,
                    mDefaultDetails?.substring(7, 8)
                )
            )
        }
        setupHoofs()
    }

    override fun sendNewRecord() {
        val bundle = bundleOf(
            EDIT_DIALOG_DOC_ID to documentId,
            EDIT_DIALOG_DATE to mActionDate?.time,
            EDIT_DIALOG_DETAILS to getEncodedHoofs() + mView?.details?.text.toString()
        )
        val intent = Intent().putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    override fun getLayoutId() = R.layout.dialog_edit_pedicure

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_DIALOG_DETAILS, getEncodedHoofs() + mView?.details?.text.toString())
    }

    private fun setupHoofs() {
        mView?.run {
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