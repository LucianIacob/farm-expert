/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 8/4/19 10:52 PM.
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
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.crashlytics.android.Crashlytics
import com.farmexpert.android.R
import com.farmexpert.android.activities.ConfigurationActivity
import com.farmexpert.android.app.FarmExpertApplication
import com.farmexpert.android.dialogs.BaseAddRecordDialogFragment
import com.farmexpert.android.fragments.BaseFragment
import com.farmexpert.android.model.Reminder
import com.farmexpert.android.planner.adapter.PlannerAdapter
import com.farmexpert.android.planner.fragments.dialog.AddPlannerItemDialogFragment
import com.farmexpert.android.planner.model.PlannerContainer
import com.farmexpert.android.planner.model.PlannerItem
import com.farmexpert.android.planner.transformer.PlannerDataTransformer
import com.farmexpert.android.utils.*
import com.farmexpert.android.viewmodel.PlannerDateViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.fragment_planner.*
import kotlinx.android.synthetic.main.fragment_planner_section.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.error
import org.jetbrains.anko.toast
import java.util.*

abstract class BasePlannerFragment : BaseFragment() {

    private var snapshotListener: ListenerRegistration? = null
    protected lateinit var adapter: PlannerAdapter

    private var plannerDateViewModel: PlannerDateViewModel? = null

    private var plannerData = mutableMapOf(
        PLANNER_DATA_REMINDERS to emptyList<PlannerItem>(),
        PLANNER_DATA_ANIMALS to emptyList()
    )

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
        PlannerAdapter(
            clickListener = { handleClick(it) },
            longClickListener = { handleLongClick(it) }
        ).run {
            adapter = this
            plannerRecycler.adapter = this
        }
    }

    private fun handleClick(clickDirections: NavDirections) {
        NavigationConstants.SHOULD_RESET_PLANNER_DATE = false
        parentFragment?.findNavController()?.navigate(clickDirections)
    }

    private fun handleLongClick(reminderId: String) {

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
        parentFragment?.childFragmentManager?.let { fragmentManager ->
            AddPlannerItemDialogFragment.getInstance(plannerDateViewModel?.getDate()?.value).also {
                it.setTargetFragment(this, ADD_PLANNER_ITEM_RQ)
                it.show(fragmentManager, AddPlannerItemDialogFragment.TAG)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data?.extras == null) return
        when (requestCode) {
            ADD_PLANNER_ITEM_RQ -> data.extras?.let { insertPlannerItem(it) }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun insertPlannerItem(extras: Bundle) {
        val details = extras.getString(AddPlannerItemDialogFragment.REMINDER_DIALOG_DETAILS, "")
        val date = Date(extras.getLong(BaseAddRecordDialogFragment.ADD_DIALOG_DATE))

        val reminder = Reminder(
            reminderDate = Timestamp(date),
            details = details,
            createdBy = currentUser?.uid,
            holderParent = getPlannerContainer().name
        )

        loadingShow()
        farmReference.collection(FirestorePath.Collections.REMINDERS)
            .add(reminder)
            .addOnSuccessListener { parentFragment?.rootLayout?.snackbar(R.string.item_added) }
            .addOnFailureListener { exception ->
                alert(message = R.string.err_adding_record)
                error { exception }
                Crashlytics.logException(exception)
            }
            .addOnCompleteListener { loadingHide() }
    }

    abstract fun getPlannerContainer(): PlannerContainer

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
            .whereEqualTo(FirestorePath.Reminder.HOLDER_PARENT, getPlannerContainer().name)
            .addSnapshotListener { querySnapshot, exception ->
                loadingHide()
                if (exception != null) {
                    error { exception }
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
        adapterData.takeIf { it.isNotEmpty() }?.let {
            emptyList?.visibility = View.GONE
        } ?: run {
            emptyList?.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        plannerDateViewModel?.getDate()?.removeObservers(this)
        add_reminder_btn.setOnClickListener(null)
    }

    companion object {
        const val ADD_PLANNER_ITEM_RQ = 6374
        const val PLANNER_DATA_REMINDERS = "com.farmexpert.android.PlannerData.Reminders"
        const val PLANNER_DATA_ANIMALS = "com.farmexpert.android.PlannerData.Animals"
    }

}
