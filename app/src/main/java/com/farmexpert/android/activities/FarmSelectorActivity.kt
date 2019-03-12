/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/12/19 10:24 AM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmexpert.android.R
import com.farmexpert.android.adapter.FarmSelectorAdapter
import com.farmexpert.android.model.Farm
import com.farmexpert.android.utils.FirestorePath
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_farm_selector.*
import org.jetbrains.anko.startActivity

class FarmSelectorActivity : AppCompatActivity() {

    companion object {
        const val KEY_CURRENT_FARM_ID = "com.farmexpert.android.FarmID"
    }

    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farm_selector)
        firestore = FirebaseFirestore.getInstance()
        currentUser = FirebaseAuth.getInstance().currentUser!!
        initFarmsList()
    }

    private fun initFarmsList() {
        val query: Query = firestore
            .collection(FirestorePath.Collections.FARMS)
            .whereArrayContains(FirestorePath.Farm.USERS, currentUser.uid)
            .orderBy(FirestorePath.Farm.NAME, Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions.Builder<Farm>()
            .setQuery(query) { it.toObject(Farm::class.java)!!.apply { id = it.id } }
            .setLifecycleOwner(this)
            .build()

        val adapter = object : FarmSelectorAdapter(options, { farm -> farmClicked(farm) }) {
            override fun onDataChanged() {
                farmListContainer.visibility = if (itemCount != 0) View.VISIBLE else View.GONE
            }
        }

        subscribedFarms.layoutManager = LinearLayoutManager(this)
        subscribedFarms.adapter = adapter
    }

    private fun farmClicked(farm: Farm) {
        farm.id?.let { storeFarmId(it) }
    }

    override fun onStart() {
        super.onStart()
        createFarmButton.setOnClickListener { createFarm() }
        joinFarmButton.setOnClickListener { joinFarm() }
    }

    private fun createFarm() {
        clearViewErrors()

        val farmName = farmName.text.toString()
        val accessCode = accessCode.text.toString()

        if (validInputs(farmName, accessCode)) {
            val farm = Farm(currentUser.uid, farmName, accessCode, arrayListOf(currentUser.uid))

            loadingProgressBar.visibility = View.VISIBLE
            checkNameAlreadyExists(farmName,
                { exists ->
                    if (exists) {
                        loadingProgressBar.visibility = View.INVISIBLE
                        displayDialog("Farm name already exists", "Change name")
                    } else saveFarm(farm)
                },
                { ex ->
                    loadingProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
                })
        }
    }

    private fun displayDialog(
        title: String,
        okButton: String,
        cancelButton: String? = null,
        ok: (() -> Unit)? = null,
        cancel: (() -> Unit)? = null
    ) {
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setPositiveButton(okButton) { _, _ -> ok?.let { ok() } }

        cancelButton?.let { builder.setNegativeButton(it) { _, _ -> cancel?.let { cancel() } } }

        builder.create().show()
    }

    private fun saveFarm(farm: Farm) {
        firestore.collection(FirestorePath.Collections.FARMS)
            .add(farm)
            .addOnSuccessListener { documentReference ->
                loadingProgressBar.visibility = View.INVISIBLE
                storeFarmId(documentReference.id)
            }
            .addOnFailureListener { e ->
                loadingProgressBar.visibility = View.INVISIBLE
                Log.w("FarmSelector", "Error adding document", e)
            }
    }

    private fun checkNameAlreadyExists(farmName: String, listener: (Boolean) -> Unit, failure: (Exception) -> Unit) {
        firestore.collection(FirestorePath.Collections.FARMS)
            .whereEqualTo(FirestorePath.Farm.NAME, farmName)
            .get()
            .addOnSuccessListener { farms -> listener(!farms.isEmpty) }
            .addOnFailureListener { ex -> failure(ex) }
    }

    private fun validInputs(farmName: String, accessCode: String): Boolean {
        var valid = true
        if (farmName.trim().isEmpty()) {
            farmNameHolder.error = getString(R.string.err_empty_farm_name)
            valid = false
        }
        if (accessCode.trim().length < 4) {
            accessCodeHolder.error = getString(R.string.err_access_code_invalid)
            valid = false
        }

        return valid
    }

    private fun clearViewErrors() {
        farmNameHolder.error = null
        accessCodeHolder.error = null
    }

    private fun joinFarm() {
        clearViewErrors()

        val farmName = farmName.text.toString()
        val accessCode = accessCode.text.toString()

        if (validInputs(farmName, accessCode)) {
            loadingProgressBar.visibility = View.VISIBLE
            checkFarmExists(farmName, accessCode,
                { exists, farmId ->
                    if (exists) {
                        updateFarm(farmId!!)
                    } else {
                        loadingProgressBar.visibility = View.INVISIBLE
                        displayDialog(
                            "Farm '$farmName' does not exists or the access code is incorrect",
                            "Review inputs"
                        )
                    }
                },
                { ex ->
                    loadingProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
                })
        }
    }

    private fun updateFarm(farmId: String) {
        firestore.collection(FirestorePath.Collections.FARMS)
            .document(farmId)
            .update(FirestorePath.Farm.USERS, FieldValue.arrayUnion(currentUser.uid))
            .addOnSuccessListener {
                loadingProgressBar.visibility = View.INVISIBLE
                storeFarmId(farmId)
            }
            .addOnFailureListener {
                loadingProgressBar.visibility = View.INVISIBLE
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }

    private fun storeFarmId(farmId: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit { putString(KEY_CURRENT_FARM_ID, farmId) }
        startActivity<MainActivity>()
        finish()
    }

    private fun checkFarmExists(
        farmName: String,
        accessCode: String,
        listener: (Boolean, String?) -> Unit,
        failure: (Exception) -> Unit
    ) {
        firestore.collection(FirestorePath.Collections.FARMS)
            .whereEqualTo(FirestorePath.Farm.NAME, farmName)
            .whereEqualTo(FirestorePath.Farm.ACCESS_CODE, accessCode)
            .get()
            .addOnSuccessListener { snapshots ->
                if (!snapshots.isEmpty) {
                    val document = snapshots.documents[0]
                    listener(true, document.id)
                } else {
                    listener(false, null)
                }
            }
            .addOnFailureListener { ex -> failure(ex) }
    }

    override fun onPause() {
        super.onPause()
        createFarmButton.setOnClickListener(null)
    }

}
