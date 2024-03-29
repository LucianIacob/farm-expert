/*
 * Developed by Lucian Iacob.
 * Romania, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:35 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.annotation.StringRes
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farmexpert.android.R
import com.farmexpert.android.adapter.GraphAdapter
import com.farmexpert.android.adapter.holder.BaseMasterHolder
import com.farmexpert.android.model.BaseEntity
import com.farmexpert.android.utils.*
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_graph_master.*
import java.util.*


abstract class BaseMasterFragment<ModelClass : BaseEntity, ModelHolder : BaseMasterHolder<ModelClass>>(
    @StringRes private val titleRes: Int
) : BaseFragment(R.layout.fragment_graph_master) {

    abstract val holderLayoutRes: Int
    abstract val snapshotParser: SnapshotParser<ModelClass>
    abstract val headerLayoutRes: Int
    abstract val filterField: String

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: GraphAdapter<ModelClass, ModelHolder>

    private var layoutState: Parcelable? = null
    private var selectedYear: String = Calendar.getInstance().get(Calendar.YEAR).toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        graphHeader.layoutResource = headerLayoutRes
        graphHeader.inflate()

        PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_LAYOUT_STATE, null)?.let {
                layoutState = Gson().fromJson(it, LinearLayoutManager.SavedState::class.java)
                PreferenceManager.getDefaultSharedPreferences(context)
                    .edit { remove(KEY_LAYOUT_STATE) }
            }

        layoutManager = LinearLayoutManager(context)
        adapter = GraphAdapter(holderLayoutRes, ::createHolder)
        with(graphRecycler) {
            layoutManager = this@BaseMasterFragment.layoutManager
            adapter = this@BaseMasterFragment.adapter
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.run {
            getString(KEY_SELECTED_YEAR)?.let { selectedYear = it }
            layoutState = getParcelable(KEY_LAYOUT_STATE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val now = Calendar.getInstance()
        val yearRange = resources.getInteger(R.integer.graph_year_count)
        for (i in yearRange downTo 1) {
            menu.add(
                Menu.NONE,
                now.get(Calendar.YEAR),
                yearRange - i,
                now.get(Calendar.YEAR).toString()
            )
            now.add(Calendar.YEAR, -1)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) return super.onOptionsItemSelected(item)

        selectedYear = item.title.toString()
        retrieveData()
        return true
    }

    override fun onStart() {
        super.onStart()
        retrieveData()
    }

    private fun retrieveData() {
        updateTitle()
        loadingShow()
        val queryRangeStart = AppUtils.getStartOfTheYear(selectedYear)
        val queryRangeEnd = AppUtils.getEndOfTheYear(selectedYear)

        getCollectionRef()
            .whereGreaterThanOrEqualTo(filterField, queryRangeStart)
            .whereLessThanOrEqualTo(filterField, queryRangeEnd)
            .get()
            .addLoggableFailureListener {
                alert(message = R.string.err_retrieving_items)
            }
            .addOnSuccessListener { documents ->
                val adapterData = transformData(documents)
                adapter.data = adapterData
                empty_list?.isVisible = adapterData.isEmpty()
                if (adapterData.isNotEmpty()) {
                    layoutState?.let { layoutManager.onRestoreInstanceState(it) }
                }
            }
            .addOnCompleteListener { loadingHide() }
    }

    private fun updateTitle() {
        val stringBuilder = StringBuilder(getString(titleRes))
            .append(" ")
            .append(selectedYear)
        setTitle(stringBuilder.toString())
    }

    protected val navigationListener: (() -> Unit) = {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit {
                val state = Gson()
                    .toJson(layoutManager.onSaveInstanceState() as LinearLayoutManager.SavedState)
                putString(KEY_LAYOUT_STATE, state)
            }
    }

    abstract fun transformData(documents: QuerySnapshot?): Map<String, List<ModelClass>>

    abstract fun getCollectionRef(): Query

    abstract fun createHolder(view: View): ModelHolder

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_SELECTED_YEAR, selectedYear)
        if (::layoutManager.isInitialized) {
            outState.putParcelable(KEY_LAYOUT_STATE, layoutManager.onSaveInstanceState())
        }
    }

    companion object {
        const val KEY_SELECTED_YEAR = "com.farmexpert.android.masterscreen.SelectedYear"
        const val KEY_LAYOUT_STATE = "com.farmexpert.android.GraphLayoutState"
    }
}
