/*
 * Developed by Lucian Iacob.
 * Romania, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:35 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.planner.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.farmexpert.android.R
import com.farmexpert.android.activities.ConfigurationActivity
import com.farmexpert.android.dialogs.AddPlannerItemDialogFragment
import com.farmexpert.android.dialogs.BaseDialogFragment
import com.farmexpert.android.fragments.BaseFragment
import com.farmexpert.android.model.Reminder
import com.farmexpert.android.planner.adapter.PlannerAdapter
import com.farmexpert.android.planner.model.PlannerContainer
import com.farmexpert.android.planner.model.PlannerItem
import com.farmexpert.android.planner.transformer.PlannerDataTransformer
import com.farmexpert.android.utils.*
import com.farmexpert.android.viewmodel.PlannerDateViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.fragment_planner.*
import kotlinx.android.synthetic.main.fragment_planner_section.*
import java.util.*

abstract class BasePlannerFragment(@StringRes private val headerResId: Int) :
    BaseFragment(R.layout.fragment_planner_section) {

    abstract val getPlannerContainer: PlannerContainer
    private val plannerDateViewModel: PlannerDateViewModel by viewModels({ requireParentFragment() })

    private var snapshotListener: ListenerRegistration? = null

    protected lateinit var adapter: PlannerAdapter

    private var plannerData = mutableMapOf(
        PLANNER_DATA_REMINDERS to emptyList<PlannerItem>(),
        PLANNER_DATA_ANIMALS to emptyList()
    )

    protected val farmTimelinePrefs: SharedPreferences by lazy {
        activity?.getSharedPreferences(
            ConfigurationActivity.FARM_TIMELINE_PREFS,
            Context.MODE_PRIVATE
        ) ?: throw IllegalStateException()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        containerHeader.text = getString(headerResId)
        PlannerAdapter(
            clickListener = { handleClick(it) },
            longClickListener = { handleLongClick() }
        ).run {
            adapter = this
            plannerRecycler.adapter = this
        }
    }

    private fun handleClick(clickDirections: NavDirections) {
        NavigationConstants.SHOULD_RESET_PLANNER_DATE = false
        parentFragment?.findNavController()?.navigate(clickDirections)
    }

    private fun handleLongClick() {
    }

    override fun onResume() {
        super.onResume()
        plannerDateViewModel
            .getDate()
            .observe(
                this,
                { date -> retrieveDataForDate(date) }
            )
        add_reminder_btn.setOnClickListener { displayAddReminderDialog() }
    }

    private fun displayAddReminderDialog() {
        parentFragment?.childFragmentManager?.let { fragmentManager ->
            AddPlannerItemDialogFragment.getInstance(plannerDateViewModel.getDate().value).also {
                it.setTargetFragment(this, ADD_PLANNER_ITEM_RQ)
                it.isCancelable = false
                it.show(fragmentManager, AddPlannerItemDialogFragment.TAG)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data?.extras == null) return
        when (requestCode) {
            ADD_PLANNER_ITEM_RQ -> data.extras?.let { insertPlannerItem(it) }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun insertPlannerItem(extras: Bundle) {
        val details = extras.getString(AddPlannerItemDialogFragment.REMINDER_DIALOG_DETAILS, "")
        val date = Date(extras.getLong(BaseDialogFragment.DIALOG_DATE))

        val reminder = Reminder(
            reminderDate = Timestamp(date),
            details = details,
            createdBy = currentUser?.uid,
            holderParent = getPlannerContainer.name
        )

        loadingShow()
        farmReference.collection(FirestorePath.Collections.REMINDERS)
            .add(reminder)
            .addOnSuccessListener { parentFragment?.rootLayout?.snackbar(R.string.item_added) }
            .addLoggableFailureListener {
                alert(message = R.string.err_adding_record)
            }
            .addOnCompleteListener { loadingHide() }
    }

    open fun retrieveDataForDate(date: Date) {
        snapshotListener?.remove()
        retrieveRemindersForDate(date)
    }

    private fun retrieveRemindersForDate(date: Date) {
        val startDate = date.shift(jumpTo = TimeOfTheDay.START)
        val endDate = date.shift(jumpTo = TimeOfTheDay.END)

        loadingShow()
        snapshotListener = farmReference.collection(FirestorePath.Collections.REMINDERS)
            .whereGreaterThanOrEqualTo(FirestorePath.Reminder.REMINDER_DATE, Timestamp(startDate))
            .whereLessThanOrEqualTo(FirestorePath.Reminder.REMINDER_DATE, Timestamp(endDate))
            .whereEqualTo(FirestorePath.Reminder.HOLDER_PARENT, getPlannerContainer.name)
            .addSnapshotListener { querySnapshot, exception ->
                loadingHide()
                if (exception != null) {
                    error(exception)
                    activity?.toast(R.string.err_retrieving_reminders)
                } else if (querySnapshot != null) {
                    val reminders = PlannerDataTransformer.transformReminders(querySnapshot)
                    dataRetrievedSuccessfully(reminders, PLANNER_DATA_REMINDERS)
                }
            }
    }

    open fun dataRetrievedSuccessfully(plannerList: List<PlannerItem>, dataType: String) {
        plannerData[dataType] = plannerList

        val adapterData = (plannerData[PLANNER_DATA_ANIMALS] as List<PlannerItem>)
            .plus((plannerData[PLANNER_DATA_REMINDERS] as List<PlannerItem>))

        adapter.data = adapterData
        emptyList?.isVisible = adapterData.isEmpty()
    }

    override fun onPause() {
        super.onPause()
        plannerDateViewModel.getDate().removeObservers(this)
        add_reminder_btn.setOnClickListener(null)
    }

    companion object {
        const val ADD_PLANNER_ITEM_RQ = 6374
        const val PLANNER_DATA_REMINDERS = "com.farmexpert.android.PlannerData.Reminders"
        const val PLANNER_DATA_ANIMALS = "com.farmexpert.android.PlannerData.Animals"
    }

}
