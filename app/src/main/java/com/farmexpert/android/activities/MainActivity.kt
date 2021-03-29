/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/24/19 9:30 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.core.view.isInvisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.farmexpert.android.R
import com.farmexpert.android.activities.FarmSelectorActivity.Companion.KEY_CURRENT_FARM_NAME
import com.farmexpert.android.utils.*
import com.firebase.ui.auth.AuthUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_dashboard.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment)
            ?.navController
            ?.let { setupNavigationDrawer(it) }
    }

    override fun onStart() {
        super.onStart()
        setupNavHeader()
    }

    private fun setupNavHeader() {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            user.photoUrl?.toString()?.isNotEmpty()?.let {
                Picasso.get()
                    .load(user.photoUrl)
                    .transform(CircleTransform())
                    .fit()
                    .centerCrop()
                    .into(userIcon, object : Callback.EmptyCallback() {
                        override fun onError(e: Exception) {
                            error(e)
                        }
                    })
            }
            user.phoneNumber?.takeIfNotBlank()?.let { userName.text = it }
            user.displayName?.takeIfNotBlank()?.let { userName.text = it }
            user.email?.takeIfNotBlank()?.let { userEmail.text = it }
        }

        userIcon.setOnClickListener { openUserProfileScreen() }
        profileSettingsBtn.setOnClickListener { openUserProfileScreen() }

        PreferenceManager.getDefaultSharedPreferences(this)
            .getString(KEY_CURRENT_FARM_NAME, null)
            ?.takeIfNotBlank()
            ?.let { farmName.text = it }
    }

    private fun openUserProfileScreen() {
        drawer_layout.closeDrawer(nav_view, true)
        startActivity<UserProfileActivity>()
    }

    private fun setupNavigationDrawer(navController: NavController) {
        setupActionBarWithNavController(navController, drawer_layout)
        nav_view.setNavigationItemSelectedListener {
            val handled = it.onNavDestinationSelected(navController)
            when (it.itemId) {
                R.id.logOut -> logOutRequested()
                R.id.changeFarm -> changeFarmRequested()
            }

            drawer_layout.closeDrawer(nav_view, true)
            return@setNavigationItemSelectedListener handled
        }
    }

    private fun changeFarmRequested() {
        MaterialAlertDialogBuilder(this)
            .setMessage(R.string.change_farm_confirmation_message)
            .setPositiveButton(R.string.confirm_button) { _, _ ->
                PreferenceManager.getDefaultSharedPreferences(this)
                    .edit { remove(FarmSelectorActivity.KEY_CURRENT_FARM_ID) }
                startActivity<FarmSelectorActivity>()
                finish()
            }
            .setNegativeButton(R.string.fui_cancel) { _, _ -> }
            .create()
            .run {
                setOnShowListener {
                    getButton(BUTTON_POSITIVE).applyFarmexpertStyle(
                        context = context,
                        redButton = true
                    )
                }
                show()
            }
    }

    private fun logOutRequested() {
        MaterialAlertDialogBuilder(this)
            .setMessage(R.string.logout_confirmation_message)
            .setPositiveButton(R.string.confirm_button) { _, _ ->
                logOutConfirmed()
            }
            .setNegativeButton(R.string.fui_cancel) { _, _ -> }
            .create()
            .run {
                setOnShowListener {
                    getButton(BUTTON_POSITIVE).applyFarmexpertStyle(
                        context = context,
                        redButton = true
                    )
                }
                show()
            }
    }

    private fun logOutConfirmed() {
        setLoadingVisibility(true)
        AuthUI.getInstance()
            .signOut(this)
            .addOnSuccessListener {
                PreferenceManager.getDefaultSharedPreferences(this)
                    .edit { remove(FarmSelectorActivity.KEY_CURRENT_FARM_ID) }
                startActivity<AuthenticationActivity>()
                finish()
            }
            .addLoggableFailureListener {
                alert(R.string.err_logging_out)
            }
            .addOnCompleteListener { setLoadingVisibility(false) }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        NavigationUI.onNavDestinationSelected(
            item,
            Navigation.findNavController(this, R.id.nav_host_fragment)
        ) || super.onOptionsItemSelected(item)

    override fun onSupportNavigateUp(): Boolean =
        NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.nav_host_fragment),
            drawer_layout
        )

    fun setLoadingVisibility(visible: Boolean) {
        runOnUiThread { loadingView.isInvisible = !visible }
    }
}
