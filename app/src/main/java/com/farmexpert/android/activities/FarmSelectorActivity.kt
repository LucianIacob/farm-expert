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
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.farmexpert.android.R
import com.farmexpert.android.adapter.FarmSelectorAdapter
import com.farmexpert.android.model.Farm
import com.farmexpert.android.utils.FirestorePath
import com.farmexpert.android.utils.invisible
import com.farmexpert.android.utils.visible
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.FirebaseCrashlytics
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

    private var firestore: FirebaseFirestore = Firebase.firestore
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farm_selector)
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
            val farm = Farm(
                owner = currentUser.uid,
                name = farmName,
                accessCode = accessCode,
                users = arrayListOf(currentUser.uid)
            )

            loadingProgressBar.visible()

            checkNameAlreadyExists(
                farmName = farmName,
                successListener = { exists ->
                    if (exists) {
                        loadingProgressBar.invisible()
                        displayDialog(
                            getString(R.string.farm_already_exists),
                            R.string.change_farm_name
                        )
                    } else saveFarm(farm)
                },
                failureListener = { exception ->
                    loadingProgressBar.invisible()
                    exception.message?.let { longToast(it) }
                }
            )
        }
    }

    private fun displayDialog(
        message: String,
        okButton: Int,
        okListener: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.error)
            .setMessage(message)
            .setPositiveButton(okButton) { _, _ -> okListener?.let { okListener() } }
            .show()
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
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
    }

    private fun checkNameAlreadyExists(
        farmName: String,
        successListener: (Boolean) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        firestore.collection(FirestorePath.Collections.FARMS)
            .whereEqualTo(FirestorePath.Farm.NAME, farmName)
            .get()
            .addOnSuccessListener { farms -> successListener(!farms.isEmpty) }
            .addOnFailureListener { exception ->
                failureListener(exception)
                FirebaseCrashlytics.getInstance().recordException(exception)
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
            checkFarmExists(
                farmName = farmName,
                accessCode = accessCode,
                successListener = { exists, farm ->
                    if (exists && farm != null) {
                        storeFarmDetails(farm)
                        updateFarm(farm)
                    } else {
                        loadingProgressBar.invisible()
                        displayDialog(
                            getString(R.string.farm_does_not_exists, farmName),
                            R.string.review_inputs
                        )
                    }
                },
                failureListener = { ex ->
                    loadingProgressBar.invisible()
                    Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
                })
        }
    }

    private fun storeFarmDetails(farm: Farm) {
        getSharedPreferences(ConfigurationActivity.FARM_TIMELINE_PREFS, Context.MODE_PRIVATE)
            .edit {
                putInt(getString(R.string.pref_heating_start_key), farm.heatingStartsAt)
                putInt(getString(R.string.pref_heating_end_key), farm.heatingEndsAt)
                putInt(getString(R.string.pref_gestation_key), farm.gestationControl)
                putInt(
                    getString(R.string.pref_physiological_control_key),
                    farm.physiologicalControl
                )
                putInt(getString(R.string.pref_disinfection_key), farm.disinfectionBeforeBirth)
                putInt(getString(R.string.pref_vaccin1_before_birth_key), farm.vaccin1BeforeBirth)
                putInt(getString(R.string.pref_vaccin2_before_birth_key), farm.vaccin2BeforeBirth)
                putInt(getString(R.string.pref_vaccin3_before_birth_key), farm.vaccin3BeforeBirth)
                putInt(getString(R.string.pref_vaccin_after_birth_key), farm.vaccin3BeforeBirth)
                putInt(getString(R.string.pref_gestation_length_key), farm.gestationLength)
            }

        PreferenceManager
            .getDefaultSharedPreferences(this)
            .edit { putBoolean(ConfigurationActivity.KEY_CONFIGS_ACCEPTED, true) }
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
                    FirebaseCrashlytics.getInstance().recordException(exception)
                }
        }
    }

    private fun storeFarmId(farmId: String, name: String) {
        PreferenceManager.getDefaultSharedPreferences(this).edit {
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
        successListener: (Boolean, Farm?) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        firestore.collection(FirestorePath.Collections.FARMS)
            .whereEqualTo(FirestorePath.Farm.NAME, farmName)
            .whereEqualTo(FirestorePath.Farm.ACCESS_CODE, accessCode)
            .get()
            .addOnSuccessListener { snapshots ->
                if (!snapshots.isEmpty) {
                    val document = snapshots.documents[0]
                    document.toObject<Farm>()?.also { farm ->
                        farm.id = document.id
                        successListener(true, farm)
                    } ?: failureListener.invoke(Resources.NotFoundException())
                } else {
                    successListener(false, null)
                }
            }
            .addOnFailureListener { exception ->
                failureListener(exception)
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
    }

    companion object {
        const val KEY_CURRENT_FARM_ID = "com.farmexpert.android.FarmID"
        const val KEY_CURRENT_FARM_NAME = "com.farmexpert.android.FarmName"
    }
}
