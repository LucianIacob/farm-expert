package com.farmexpert.android.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.isInvisible
import androidx.preference.PreferenceManager
import com.farmexpert.android.R
import com.farmexpert.android.activities.FarmSelectorActivity.Companion.KEY_CURRENT_FARM_ID
import com.farmexpert.android.adapter.UserFarmsAdapter
import com.farmexpert.android.model.Farm
import com.farmexpert.android.utils.*
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_list_user_farms.*

class UserFarmsActivity : AppCompatActivity(R.layout.activity_list_user_farms) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()

        FirebaseAuth.getInstance()
            .currentUser
            ?.uid
            ?.run { initFarmList(userId = this) }
    }

    private fun initFarmList(userId: String) {
        val query: Query = Firebase.firestore
            .collection(FirestorePath.Collections.FARMS)
            .whereArrayContains(FirestorePath.Farm.USERS, userId)
            .orderBy(FirestorePath.Farm.NAME, Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions.Builder<Farm>()
            .setQuery(query) { it.toObject<Farm>()!!.apply { id = it.id } }
            .setLifecycleOwner(this)
            .build()

        subscribedFarms.adapter = UserFarmsAdapter(
            options = options,
            unsubscribeListener = { farm -> unsubscribeFrom(farm) },
            deleteListener = { farm -> deleteFarm(farm) }
        )
    }

    private fun deleteFarm(farm: Farm) {
        toast(farm.name)
    }

    private fun unsubscribeFrom(farm: Farm) {
        val isCurrentFarm = PreferenceManager
            .getDefaultSharedPreferences(this)
            .getString(KEY_CURRENT_FARM_ID, null)
            .equals(farm.id)

        alert(
            message = if (isCurrentFarm) R.string.unsubscribe_current_farm_confirmation_message
            else R.string.unsubscribe_confirmation_message,
            isCancellable = true,
            redButton = true,
            negativeButton = true,
            okListener = {
                unsubscribeConfirmed(farm, isCurrentFarm)
            }
        )
    }

    private fun unsubscribeConfirmed(farm: Farm, isCurrentFarm: Boolean) {
        farm.id?.let { farmId ->
            loadingView.isInvisible = false

            Firebase.firestore
                .collection(FirestorePath.Collections.FARMS)
                .document(farmId)
                .update(
                    FirestorePath.Farm.USERS,
                    FieldValue.arrayRemove(FirebaseAuth.getInstance().currentUser?.uid)
                )
                .addOnSuccessListener {
                    if (isCurrentFarm) {
                        PreferenceManager.getDefaultSharedPreferences(this)
                            .edit { remove(KEY_CURRENT_FARM_ID) }
                        startActivity<FarmSelectorActivity>()
                        finishAffinity()
                    }
                }
                .addLoggableFailureListener { exception ->
                    exception.message?.let { message -> alert(message) }
                }
                .addOnCompleteListener { loadingView.isInvisible = true }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
