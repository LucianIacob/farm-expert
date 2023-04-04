/*
 * Developed by Lucian Iacob.
 * Romania, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:49 PM.
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
import com.farmexpert.android.databinding.ActivityMainBinding
import com.farmexpert.android.utils.*
import com.firebase.ui.auth.AuthUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

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
                    .into(binding.navHeader.userIcon, object : Callback.EmptyCallback() {
                        override fun onError(e: Exception) {
                            error(e)
                        }
                    })
            }
            user.phoneNumber?.takeIfNotBlank()?.let { binding.navHeader.userName.text = it }
            user.displayName?.takeIfNotBlank()?.let { binding.navHeader.userName.text = it }
            user.email?.takeIfNotBlank()?.let { binding.navHeader.userEmail.text = it }
        }

        binding.navHeader.userIcon.setOnClickListener { openUserProfileScreen() }
        binding.navHeader.profileSettingsBtn.setOnClickListener { openUserProfileScreen() }

        PreferenceManager.getDefaultSharedPreferences(this)
            .getString(KEY_CURRENT_FARM_NAME, null)
            ?.takeIfNotBlank()
            ?.let { binding.navHeader.farmName.text = it }
    }

    private fun openUserProfileScreen() {
        binding.root.closeDrawer(binding.navView, true)
        startActivity<UserProfileActivity>()
    }

    private fun setupNavigationDrawer(navController: NavController) {
        setupActionBarWithNavController(navController, binding.root)
        binding.navView.setNavigationItemSelectedListener {
            val handled = it.onNavDestinationSelected(navController)
            when (it.itemId) {
                R.id.logOut -> logOutRequested()
                R.id.changeFarm -> changeFarmRequested()
            }

            binding.root.closeDrawer(binding.navView, true)
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
            .setNegativeButton(R.string.dialog_cancel_btn) { _, _ -> }
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
            .setNegativeButton(R.string.dialog_cancel_btn) { _, _ -> }
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
        if (binding.root.isDrawerOpen(GravityCompat.START)) {
            binding.root.closeDrawer(GravityCompat.START)
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
            binding.root
        )

    fun setLoadingVisibility(visible: Boolean) {
        runOnUiThread { binding.loadingView.isInvisible = !visible }
    }
}
