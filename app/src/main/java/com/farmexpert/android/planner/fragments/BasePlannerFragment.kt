/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 7/17/19 10:02 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.farmexpert.android.R
import com.farmexpert.android.activities.ConfigurationActivity
import com.farmexpert.android.app.FarmExpertApplication
import com.farmexpert.android.fragments.BaseFragment
import com.farmexpert.android.model.Reminder
import com.farmexpert.android.planner.adapter.PlannerAdapter
import com.farmexpert.android.planner.fragments.dialog.AddPlannerItemDialogFragment
import com.farmexpert.android.planner.model.PlannerContainer
import com.farmexpert.android.utils.FirestorePath
import com.farmexpert.android.viewmodel.PlannerDateViewModel
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.fragment_planner.*
import kotlinx.android.synthetic.main.fragment_planner_section.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.error
import org.jetbrains.anko.okButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import java.util.*

abstract class BasePlannerFragment : BaseFragment() {

    protected lateinit var adapter: PlannerAdapter

    private var plannerDateViewModel: PlannerDateViewModel? = null

    protected val farmTimelinePrefs: SharedPreferences =
        FarmExpertApplication.appContext.getSharedPreferences(
            ConfigurationActivity.FARM_TIMELINE_PREFS,
            Context.MODE_PRIVATE
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_planner_section, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        containerHeader.text = getHeaderText()
        PlannerAdapter { plannerItem -> toast(plannerItem.animalId) }.run {
            adapter = this
            plannerRecycler.adapter = this
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        parentFragment?.let {
            plannerDateViewModel = ViewModelProviders.of(it).get(PlannerDateViewModel::class.java)
        }
    }

    abstract fun getHeaderText(): String

    override fun onResume() {
        super.onResume()
        plannerDateViewModel?.getDate()
            ?.observe(this, Observer { date -> retrieveDataForDate(date) })
        add_reminder_btn.setOnClickListener { displayAddReminderDialog() }
    }

    private fun displayAddReminderDialog() {
        parentFragment?.childFragmentManager?.let {
            val addPlannerItem = AddPlannerItemDialogFragment()
            addPlannerItem.setTargetFragment(this, ADD_PLANNER_ITEM_RQ)
            addPlannerItem.show(it, AddPlannerItemDialogFragment.TAG)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data?.extras == null) return
        when (requestCode) {
            ADD_PLANNER_ITEM_RQ -> insertPlannerItem(data.extras!!)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun insertPlannerItem(extras: Bundle) {
        val details = extras.getString(AddPlannerItemDialogFragment.REMINDER_DIALOG_DETAILS, "")

        val reminder = Reminder(
            reminderDate = Timestamp(plannerDateViewModel?.getDate()?.value),
            details = details,
            createdBy = currentUser?.uid,
            holderParent = getPlannerContainer().name
        )

        loadingShow()
        farmReference.collection(FirestorePath.Collections.REMINDERS)
            .add(reminder)
            .addOnSuccessListener { parentFragment?.rootLayout?.snackbar(R.string.item_added) }
            .addOnFailureListener { e ->
                alert(message = R.string.err_adding_record) { okButton {} }.show()
                error { e }
            }
            .addOnCompleteListener { loadingHide() }
    }

    abstract fun getPlannerContainer(): PlannerContainer

    open fun retrieveDataForDate(date: Date) {

    }

    override fun onPause() {
        super.onPause()
        plannerDateViewModel?.getDate()?.removeObservers(this)
        add_reminder_btn.setOnClickListener(null)
    }

    companion object {
        const val ADD_PLANNER_ITEM_RQ = 6374
    }

}