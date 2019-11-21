/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/23/19 6:48 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.content.Context
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.crashlytics.android.Crashlytics
import com.farmexpert.android.R
import com.farmexpert.android.adapter.FarmSelectorAdapter
import com.farmexpert.android.model.Farm
import com.farmexpert.android.utils.FirestorePath
import com.farmexpert.android.utils.applyFarmexpertStyle
import com.farmexpert.android.utils.invisible
import com.farmexpert.android.utils.visible
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_farm_selector.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity

class FarmSelectorActivity : AppCompatActivity(), AnkoLogger {

    companion object {
        const val KEY_CURRENT_FARM_ID = "com.farmexpert.android.FarmID"
        const val KEY_CURRENT_FARM_NAME = "com.farmexpert.android.FarmName"
    }

    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farm_selector)
        firestore = Firebase.firestore
        FirebaseAuth.getInstance().currentUser?.let {
            currentUser = it
            initFarmsList()
        } ?: run {
            startActivity<AuthenticationActivity>()
            finish()
        }
    }

    private fun initFarmsList() {
        val query: Query = firestore
            .collection(FirestorePath.Collections.FARMS)
            .whereArrayContains(FirestorePath.Farm.USERS, currentUser.uid)
            .orderBy(FirestorePath.Farm.NAME, Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions.Builder<Farm>()
            .setQuery(query) { it.toObject<Farm>()!!.apply { id = it.id } }
            .setLifecycleOwner(this)
            .build()

        val adapter = object : FarmSelectorAdapter(options, { farm -> farmClicked(farm) }) {
            override fun onDataChanged() {
                if (itemCount != 0) {
                    recyclerViewFarms.visible()
                    subscribedFarms.visible()
                } else {
                    recyclerViewFarms.invisible()
                    subscribedFarms.invisible()
                }
            }
        }

        subscribedFarms.layoutManager = LinearLayoutManager(this)
        subscribedFarms.adapter = adapter
    }

    private fun farmClicked(farm: Farm) {
        farm.id?.let {
            storeFarmDetails(farm)
            storeFarmId(it, farm.name)
            openMainActivity()
        }
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
            val farm = Farm(currentUser.uid, farmName, accessCode, users = arrayListOf(currentUser.uid))

            loadingProgressBar.visible()
            checkNameAlreadyExists(farmName,
                { exists ->
                    if (exists) {
                        loadingProgressBar.invisible()
                        displayDialog("Farm name already exists", "Change name")
                    } else saveFarm(farm)
                },
                { ex ->
                    loadingProgressBar.invisible()
                    ex.message?.let { longToast(it) }
                })
        }
    }

    private fun displayDialog(
        title: String,
        okButton: String,
        ok: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
            .setTitle(title)
            .setPositiveButton(okButton) { _, _ -> ok?.let { ok() } }
            .create()
            .run {
                setOnShowListener {
                    getButton(BUTTON_NEGATIVE).applyFarmexpertStyle(context)
                    getButton(BUTTON_POSITIVE).applyFarmexpertStyle(context)
                }
                show()
            }
    }

    private fun saveFarm(farm: Farm) {
        firestore.collection(FirestorePath.Collections.FARMS)
            .add(farm)
            .addOnSuccessListener { documentReference ->
                loadingProgressBar.invisible()
                storeFarmId(documentReference.id, farm.name)
                openFarmConfigurationsActivity()
            }
            .addOnFailureListener { exception ->
                loadingProgressBar.invisible()
                error { exception }
                Crashlytics.logException(exception)
            }
    }

    private fun checkNameAlreadyExists(farmName: String, listener: (Boolean) -> Unit, failure: (Exception) -> Unit) {
        firestore.collection(FirestorePath.Collections.FARMS)
            .whereEqualTo(FirestorePath.Farm.NAME, farmName)
            .get()
            .addOnSuccessListener { farms -> listener(!farms.isEmpty) }
            .addOnFailureListener { exception ->
                failure(exception)
                Crashlytics.logException(exception)
            }
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
            loadingProgressBar.visible()
            checkFarmExists(farmName, accessCode,
                { exists, farm ->
                    if (exists && farm != null) {
                        storeFarmDetails(farm)
                        updateFarm(farm)
                    } else {
                        loadingProgressBar.invisible()
                        displayDialog(
                            "Farm '$farmName' does not exists or the access code is incorrect",
                            "Review inputs"
                        )
                    }
                },
                { ex ->
                    loadingProgressBar.invisible()
                    Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
                })
        }
    }

    private fun storeFarmDetails(farm: Farm) {
        val prefs = getSharedPreferences(ConfigurationActivity.FARM_TIMELINE_PREFS, Context.MODE_PRIVATE)
        prefs.edit {
            putInt(getString(R.string.pref_heating_start_key), farm.heatingStartsAt)
            putInt(getString(R.string.pref_heating_end_key), farm.heatingEndsAt)
            putInt(getString(R.string.pref_gestation_key), farm.gestationControl)
            putInt(getString(R.string.pref_physiological_control_key), farm.physiologicalControl)
            putInt(getString(R.string.pref_disinfection_key), farm.disinfectionBeforeBirth)
            putInt(getString(R.string.pref_vaccin1_before_birth_key), farm.vaccin1BeforeBirth)
            putInt(getString(R.string.pref_vaccin2_before_birth_key), farm.vaccin2BeforeBirth)
            putInt(getString(R.string.pref_vaccin3_before_birth_key), farm.vaccin3BeforeBirth)
            putInt(getString(R.string.pref_vaccin_after_birth_key), farm.vaccin3BeforeBirth)
            putInt(getString(R.string.pref_gestation_length_key), farm.gestationLength)
        }
    }

    private fun updateFarm(farm: Farm) {
        farm.id?.let { farmId ->
            firestore.collection(FirestorePath.Collections.FARMS)
                .document(farmId)
                .update(FirestorePath.Farm.USERS, FieldValue.arrayUnion(currentUser.uid))
                .addOnSuccessListener {
                    loadingProgressBar.invisible()
                    storeFarmId(farmId, farm.name)
                    openMainActivity()
                }
                .addOnFailureListener { exception ->
                    loadingProgressBar.invisible()
                    exception.message?.let { longToast(it) }
                    Crashlytics.logException(exception)
                }
        }
    }

    private fun storeFarmId(farmId: String, name: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit {
            putString(KEY_CURRENT_FARM_ID, farmId)
            putString(KEY_CURRENT_FARM_NAME, name)
        }
    }

    private fun openMainActivity() {
        startActivity<MainActivity>()
        finish()
    }

    private fun openFarmConfigurationsActivity() {
        startActivity<ConfigurationActivity>()
        finish()
    }

    private fun checkFarmExists(
        farmName: String,
        accessCode: String,
        listener: (Boolean, Farm?) -> Unit,
        failure: (Exception) -> Unit
    ) {
        firestore.collection(FirestorePath.Collections.FARMS)
            .whereEqualTo(FirestorePath.Farm.NAME, farmName)
            .whereEqualTo(FirestorePath.Farm.ACCESS_CODE, accessCode)
            .get()
            .addOnSuccessListener { snapshots ->
                if (!snapshots.isEmpty) {
                    val document = snapshots.documents[0]
                    document.toObject<Farm>()?.also {
                        it.id = document.id
                        listener(true, it)
                    } ?: failure.invoke(Resources.NotFoundException())
                } else {
                    listener(false, null)
                }
            }
            .addOnFailureListener { exception ->
                failure(exception)
                Crashlytics.logException(exception)
            }
    }

    override fun onPause() {
        super.onPause()
        createFarmButton.setOnClickListener(null)
    }

}
