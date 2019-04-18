/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/18/19 10:14 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.DividerItemDecoration
import com.farmexpert.android.R
import com.farmexpert.android.adapter.GraphAdapter
import com.farmexpert.android.adapter.holder.BaseMasterHolder
import com.farmexpert.android.model.BaseEntity
import com.farmexpert.android.utils.AppUtils
import com.farmexpert.android.utils.hidden
import com.farmexpert.android.utils.visible
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.firestore.CollectionReference
import kotlinx.android.synthetic.main.fragment_graph_master.*
import java.util.*


abstract class BaseMasterFragment<ModelClass : BaseEntity, ModelHolder : BaseMasterHolder<ModelClass>> :
    BaseFragment() {

    private lateinit var adapter: GraphAdapter<ModelClass, ModelHolder>

    private var selectedYear: String = Calendar.getInstance().get(Calendar.YEAR).toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_graph_master, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        graphHeader.layoutResource = getHeaderLayoutRes()
        graphHeader.inflate()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val now = Calendar.getInstance()
        val yearRange = resources.getInteger(R.integer.grafic_year_count)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) return super.onOptionsItemSelected(item)

        selectedYear = item.title.toString()
        retrieveData()
        return true
    }

    override fun onViewReady() {
        retrieveData()
    }

    private fun retrieveData() {
        loadingShow()
        val queryRangeStart = AppUtils.getStartOfTheYear(selectedYear)
        val queryRangeEnd = AppUtils.getEndOfTheYear(selectedYear)

        val query = getCollectionRef()
            .whereGreaterThanOrEqualTo(getFilterField(), queryRangeStart)
            .whereLessThanOrEqualTo(getFilterField(), queryRangeEnd)

        val options = FirestoreRecyclerOptions.Builder<ModelClass>()
            .setQuery(query, snapshotParser)
            .build()

        adapter = object : GraphAdapter<ModelClass, ModelHolder>(
            options,
            getHolderLayoutRes(),
            ::createHolder
        ) {
            override fun onDataChanged() {
                loadingHide()
                if (itemCount != 0) {
                    empty_list?.hidden()
                } else {
                    empty_list?.visible()
                }
            }
        }

        with(graphRecycler) {
            this.adapter = this@BaseMasterFragment.adapter
            this.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
        adapter.readyForListening()
    }

    abstract fun getHolderLayoutRes(): Int

    abstract val snapshotParser: SnapshotParser<ModelClass>

    abstract fun getFilterField(): String

    abstract fun getCollectionRef(): CollectionReference

    abstract fun getHeaderLayoutRes(): Int

    abstract fun createHolder(view: View): ModelHolder

    override fun onStart() {
        super.onStart()
        if (::adapter.isInitialized) {
            adapter.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        if (::adapter.isInitialized) {
            adapter.stopListening()
        }
    }
}
