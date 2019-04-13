/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/13/19 9:17 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.farmexpert.android.R
import com.farmexpert.android.adapter.AnimalActionsAdapter
import com.farmexpert.android.adapter.holder.BaseDetailHolder
import com.farmexpert.android.model.BaseEntity
import com.farmexpert.android.utils.hidden
import com.farmexpert.android.utils.visible
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_animal_action_detail.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.error
import org.jetbrains.anko.noButton
import org.jetbrains.anko.okButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton

abstract class BaseDetailFragment<ModelClass : BaseEntity, ModelHolder : BaseDetailHolder<ModelClass>> :
    BaseFragment() {

    private lateinit var adapter: AnimalActionsAdapter<ModelClass, ModelHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animal_action_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(getTitleAndHolderLayout().first + " " + getAnimalId())
        prepareComponents()
    }

    private fun prepareComponents() {
        loadingShow()
        val options = FirestoreRecyclerOptions.Builder<ModelClass>()
            .setQuery(getQuery(), snapshotParser)
            .build()

        adapter = object : AnimalActionsAdapter<ModelClass, ModelHolder>(
            options,
            getTitleAndHolderLayout().second,
            ::createHolder
        ) {
            override fun onDataChanged() {
                loadingHide()
                if (itemCount != 0) {
                    placeholderText?.hidden()
                } else {
                    placeholderText?.visible()
                }
            }
        }

        actionsRecycler.adapter = adapter
    }

    abstract fun getQuery(): Query

    override fun onViewReady() {
        super.onViewReady()
        adapter.readyForListening()
    }

    abstract val snapshotParser: SnapshotParser<ModelClass>

    abstract fun createHolder(view: View): ModelHolder

    abstract fun getTitleAndHolderLayout(): Pair<String, Int>

    abstract fun getAnimalId(): String

    abstract fun getCollectionReference(): CollectionReference

    open fun addDependentData(entity: Any) {}

    fun showDeleteDialog(entity: ModelClass) {
        alert(message = R.string.delete_confirmation_request) {
            yesButton { deleteEntity(entity) }
            noButton { }
        }.show()
    }

    private fun deleteEntity(entity: ModelClass) {
        loadingShow()
        entity.id?.let {
            getCollectionReference().document(it)
                .delete()
                .addOnFailureListener { alert(message = R.string.err_deleting_item) { okButton { } }.show() }
                .addOnCompleteListener { loadingHide() }
        }
    }

    open fun validateEntity(entity: Any, listener: (Boolean) -> Unit) = listener(true)

    abstract fun constructEntityFromBundle(bundle: Bundle): Any

    abstract fun getAddRecordDialog(): DialogFragment

    abstract fun getEditRecordDialog(): DialogFragment

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onResume() {
        super.onResume()
        addButton.setOnClickListener { addRecordClicked() }
    }

    private fun addRecordClicked() {
        fragmentManager?.run {
            val addDialog = getAddRecordDialog()
            addDialog.setTargetFragment(this@BaseDetailFragment, ADD_RECORD_RQ)
            if (findFragmentByTag(addDialog::class.java.simpleName) == null) {
                addDialog.show(this, addDialog::class.java.simpleName)
            }
        }
    }

    protected fun showUpdateDialog(entityToUpdate: ModelClass) {
        fragmentManager?.run {
            val editDialog = getEditRecordDialog()
            editDialog.arguments = entityToUpdate.getEditDialogArgs()
            editDialog.setTargetFragment(this@BaseDetailFragment, UPDATE_RECORD_RQ)
            if (findFragmentByTag(editDialog::class.java.simpleName) == null) {
                editDialog.show(this, editDialog::class.java.simpleName)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data?.extras == null) return
        when (requestCode) {
            ADD_RECORD_RQ -> insertRecord(data.extras!!)
            UPDATE_RECORD_RQ -> updateRecord(data.extras!!)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun updateRecord(args: Bundle) {
        toast(args.size().toString())
    }

    private fun insertRecord(extras: Bundle) {
        loadingShow()
        val entity = constructEntityFromBundle(extras)
        validateEntity(entity) { isValid ->
            if (isValid) {
                getCollectionReference()
                    .add(entity)
                    .addOnSuccessListener { rootLayout.snackbar(R.string.item_added) }
                    .addOnFailureListener {
                        alert(message = R.string.err_adding_record) { yesButton {} }.show()
                        error { it }
                    }
                    .addOnCompleteListener { loadingHide() }
                addDependentData(entity)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    companion object {
        const val ADD_RECORD_RQ = 4455
        const val UPDATE_RECORD_RQ = 6677
    }

}
