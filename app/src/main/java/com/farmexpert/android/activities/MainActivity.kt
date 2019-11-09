/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/24/19 9:30 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.farmexpert.android.R
import com.farmexpert.android.activities.FarmSelectorActivity.Companion.KEY_CURRENT_FARM_NAME
import com.farmexpert.android.utils.CircleTransform
import com.farmexpert.android.utils.applyFarmexpertStyle
import com.farmexpert.android.utils.failureAlert
import com.farmexpert.android.utils.takeIfNotBlank
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_dashboard.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = host.navController

        setupNavigationDrawer(navController)
        setupNavHeader()
    }

    private fun setupNavHeader() {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            user.photoUrl?.toString()?.isNotEmpty()?.let {
                Picasso.get()
                    .load(user.photoUrl)
                    .transform(CircleTransform())
                    .into(userIcon, object : Callback.EmptyCallback() {
                        override fun onError(e: Exception?) {
                            Log.i("", "")
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
            .getString(KEY_CURRENT_FARM_NAME, null)?.takeIfNotBlank()?.let {
                farmName.text = it
            }
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
        AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
            .setMessage(R.string.change_farm_confirmation_message)
            .setPositiveButton(R.string.confirm_button) { _, _ ->
                PreferenceManager.getDefaultSharedPreferences(this)
                    .edit { remove(FarmSelectorActivity.KEY_CURRENT_FARM_ID) }
                startActivity<FarmSelectorActivity>()
                finish()
            }
            .setNegativeButton(R.string.fui_cancel) { _, _ -> }
            .setCancelable(false)
            .create()
            .run {
                setOnShowListener {
                    getButton(BUTTON_NEGATIVE).applyFarmexpertStyle(context)
                    getButton(BUTTON_POSITIVE).applyFarmexpertStyle(
                        context,
                        redButton = true
                    )
                }
                show()
            }
    }

    private fun logOutRequested() {
        AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
            .setMessage(R.string.logout_confirmation_message)
            .setPositiveButton(R.string.confirm_button) { _, _ ->
                logOutConfirmed()
            }
            .setNegativeButton(R.string.fui_cancel) { _, _ -> }
            .setCancelable(false)
            .create()
            .run {
                setOnShowListener {
                    getButton(BUTTON_NEGATIVE).applyFarmexpertStyle(context)
                    getButton(BUTTON_POSITIVE).applyFarmexpertStyle(
                        context,
                        redButton = true
                    )
                }
                show()
            }
    }

    private fun logOutConfirmed() {
        setLoadingVisibility(View.VISIBLE)
        AuthUI.getInstance()
            .signOut(this)
            .addOnSuccessListener {
                PreferenceManager.getDefaultSharedPreferences(this)
                    .edit { remove(FarmSelectorActivity.KEY_CURRENT_FARM_ID) }
                startActivity<FarmSelectorActivity>()
                finish()
            }
            .addOnFailureListener {
                error { it }
                failureAlert(R.string.err_logging_out)
            }
            .addOnCompleteListener { setLoadingVisibility(View.GONE) }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            Navigation.findNavController(this, R.id.nav_host_fragment)
        ) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.nav_host_fragment),
            drawer_layout
        )
    }

    fun setLoadingVisibility(visibility: Int) {
        runOnUiThread { loadingView.visibility = visibility }
    }
}
