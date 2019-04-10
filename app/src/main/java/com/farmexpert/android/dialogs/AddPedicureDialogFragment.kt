/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/10/19 9:18 AM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.farmexpert.android.R
import com.farmexpert.android.utils.AppUtils
import kotlinx.android.synthetic.main.dialog_add_pedicure.view.*

class AddPedicureDialogFragment : BaseAddRecordDialogFragment() {

    private lateinit var mNailsMap: HashMap<ImageView, Int>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = View.inflate(activity, R.layout.dialog_add_pedicure, null)
        mView.dialogDate.setOnClickListener { onChangeDateClick() }
        setupHoofs()
        setupDate()

        return AlertDialog.Builder(activity!!)
            .setView(mView)
            .setTitle(R.string.add_pedicure_title)
            .setPositiveButton(R.string.dialog_add_positive_btn) { _, _ -> addRecord() }
            .setNegativeButton(R.string.dialog_add_negative_btn, null)
            .create().also { it.setCanceledOnTouchOutside(false) }
    }

    override fun getDateView(): TextView = mView.dialogDate

    private fun setupHoofs() {
        with(mView) {

            mNailsMap = hashMapOf(
                left_top_hoof_left_nail to R.drawable.left_nail_default,
                left_top_hoof_right_nail to R.drawable.right_nail_default,
                right_top_left_nail to R.drawable.left_nail_default,
                right_top_right_nail to R.drawable.right_nail_default,
                left_bottom_hoof_left_nail to R.drawable.left_nail_default,
                left_bottom_hoof_right_nail to R.drawable.right_nail_default,
                right_bottom_left_nail to R.drawable.left_nail_default,
                right_bottom_right_nail to R.drawable.right_nail_default
            )

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

    private fun addRecord() {
        val intent = Intent()
        val bundle = Bundle()
        val hoofsEncoded = getEncodedHoofs()
        bundle.putString(ADD_DIALOG_DETAILS, hoofsEncoded + mView.details.text.toString())
        bundle.putLong(ADD_DIALOG_DATE, mSetDate.time)
        intent.putExtras(bundle)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
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