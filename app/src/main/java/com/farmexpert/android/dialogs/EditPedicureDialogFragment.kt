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
class EditPedicureDialogFragment : BaseEditRecordDialogFragment() {

    private lateinit var mDefaultDetails: String
    private lateinit var mNailsMap: HashMap<ImageView, Int>

    override fun extractAdditionalArgs() {
        mDefaultDetails = arguments!!.getString(EDIT_DIALOG_DETAILS, "")
    }

    override fun getTitle() = R.string.edit_pedicure_title

    override fun populateFields() {
        mView.details.setText(mDefaultDetails.drop(8))

        with(mView) {
            mNailsMap = hashMapOf(
                left_top_hoof_left_nail to AppUtils.populateLeftNail(
                    left_top_hoof_left_nail,
                    mDefaultDetails.substring(0, 1)
                ),
                left_top_hoof_right_nail to AppUtils.populateRightNail(
                    left_top_hoof_right_nail,
                    mDefaultDetails.substring(1, 2)
                ),
                right_top_left_nail to AppUtils.populateLeftNail(
                    right_top_left_nail,
                    mDefaultDetails.substring(2, 3)
                ),
                right_top_right_nail to AppUtils.populateRightNail(
                    right_top_right_nail,
                    mDefaultDetails.substring(3, 4)
                ),
                left_bottom_hoof_left_nail to AppUtils.populateLeftNail(
                    left_bottom_hoof_left_nail,
                    mDefaultDetails.substring(4, 5)
                ),
                left_bottom_hoof_right_nail to AppUtils.populateRightNail(
                    left_bottom_hoof_right_nail,
                    mDefaultDetails.substring(5, 6)
                ),
                right_bottom_left_nail to AppUtils.populateLeftNail(
                    right_bottom_left_nail,
                    mDefaultDetails.substring(6, 7)
                ),
                right_bottom_right_nail to AppUtils.populateRightNail(
                    right_bottom_right_nail,
                    mDefaultDetails.substring(7, 8)
                )
            )
        }
        setupHoofs()
    }

    override fun sendNewRecord() {
        val bundle = bundleOf(
            EDIT_DIALOG_DOC_ID to documentId,
            EDIT_DIALOG_DATE to mActionDate.time,
            EDIT_DIALOG_DETAILS to getEncodedHoofs() + mView.details.text.toString()
        )
        val intent = Intent()
        intent.putExtras(bundle)
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    override fun getLayoutId() = R.layout.dialog_edit_pedicure

    private fun setupHoofs() {
        with(mView) {
            left_top_hoof_left_nail.setOnClickListener {
                if (mNailsMap[left_top_hoof_left_nail] == R.drawable.left_nail_default) {
                    mNailsMap[left_top_hoof_left_nail] = R.drawable.left_nail_problem
                } else {
                    mNailsMap[left_top_hoof_left_nail] = R.drawable.left_nail_default
                }
                left_top_hoof_left_nail.setImageResource(mNailsMap[left_top_hoof_left_nail]!!)
            }

            left_top_hoof_right_nail.setOnClickListener {
                if (mNailsMap[left_top_hoof_right_nail] == R.drawable.right_nail_default) {
                    mNailsMap[left_top_hoof_right_nail] = R.drawable.right_nail_problem
                } else {
                    mNailsMap[left_top_hoof_right_nail] = R.drawable.right_nail_default
                }
                left_top_hoof_right_nail.setImageResource(mNailsMap[left_top_hoof_right_nail]!!)
            }

            right_top_left_nail.setOnClickListener {
                if (mNailsMap[right_top_left_nail] == R.drawable.left_nail_default) {
                    mNailsMap[right_top_left_nail] = R.drawable.left_nail_problem
                } else {
                    mNailsMap[right_top_left_nail] = R.drawable.left_nail_default
                }
                right_top_left_nail.setImageResource(mNailsMap[right_top_left_nail]!!)
            }

            right_top_right_nail.setOnClickListener {
                if (mNailsMap[right_top_right_nail] == R.drawable.right_nail_default) {
                    mNailsMap[right_top_right_nail] = R.drawable.right_nail_problem
                } else {
                    mNailsMap[right_top_right_nail] = R.drawable.right_nail_default
                }
                right_top_right_nail.setImageResource(mNailsMap[right_top_right_nail]!!)
            }

            left_bottom_hoof_left_nail.setOnClickListener {
                if (mNailsMap[left_bottom_hoof_left_nail] == R.drawable.left_nail_default) {
                    mNailsMap[left_bottom_hoof_left_nail] = R.drawable.left_nail_problem
                } else {
                    mNailsMap[left_bottom_hoof_left_nail] = R.drawable.left_nail_default
                }
                left_bottom_hoof_left_nail.setImageResource(mNailsMap[left_bottom_hoof_left_nail]!!)
            }

            left_bottom_hoof_right_nail.setOnClickListener {
                if (mNailsMap[left_bottom_hoof_right_nail] == R.drawable.right_nail_default) {
                    mNailsMap[left_bottom_hoof_right_nail] = R.drawable.right_nail_problem
                } else {
                    mNailsMap[left_bottom_hoof_right_nail] = R.drawable.right_nail_default
                }
                left_bottom_hoof_right_nail.setImageResource(mNailsMap[left_bottom_hoof_right_nail]!!)
            }

            right_bottom_left_nail.setOnClickListener {
                if (mNailsMap[right_bottom_left_nail] == R.drawable.left_nail_default) {
                    mNailsMap[right_bottom_left_nail] = R.drawable.left_nail_problem
                } else {
                    mNailsMap[right_bottom_left_nail] = R.drawable.left_nail_default
                }
                right_bottom_left_nail.setImageResource(mNailsMap[right_bottom_left_nail]!!)
            }

            right_bottom_right_nail.setOnClickListener {
                if (mNailsMap[right_bottom_right_nail] == R.drawable.right_nail_default) {
                    mNailsMap[right_bottom_right_nail] = R.drawable.right_nail_problem
                } else {
                    mNailsMap[right_bottom_right_nail] = R.drawable.right_nail_default
                }
                right_bottom_right_nail.setImageResource(mNailsMap[right_bottom_right_nail]!!)
            }
        }
    }

    private fun getEncodedHoofs(): String {
        val stringBuilder = StringBuilder()
        with(mView) {
            stringBuilder.append(AppUtils.getByteByNail(mNailsMap[left_top_hoof_left_nail]))
            stringBuilder.append(AppUtils.getByteByNail(mNailsMap[left_top_hoof_right_nail]))
            stringBuilder.append(AppUtils.getByteByNail(mNailsMap[right_top_left_nail]))
            stringBuilder.append(AppUtils.getByteByNail(mNailsMap[right_top_right_nail]))
            stringBuilder.append(AppUtils.getByteByNail(mNailsMap[left_bottom_hoof_left_nail]))
            stringBuilder.append(AppUtils.getByteByNail(mNailsMap[left_bottom_hoof_right_nail]))
            stringBuilder.append(AppUtils.getByteByNail(mNailsMap[right_bottom_left_nail]))
            stringBuilder.append(AppUtils.getByteByNail(mNailsMap[right_bottom_right_nail]))
        }
        return stringBuilder.toString()
    }
}