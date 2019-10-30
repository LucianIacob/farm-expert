/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 4/24/19 9:30 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.farmexpert.android.R
import com.farmexpert.android.activities.FarmSelectorActivity.Companion.KEY_CURRENT_FARM_NAME
import com.farmexpert.android.utils.CircleTransform
import com.farmexpert.android.utils.takeIfNotBlank
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_dashboard.*

class MainActivity : AppCompatActivity() {

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
            user.photoUrl?.toString()?.isNotEmpty()?.let { photoUri ->
                Picasso.get().load(user.photoUrl).transform(CircleTransform()).into(userIcon)
            }
            user.phoneNumber?.takeIfNotBlank()?.let { userName.text = it }
            user.displayName?.takeIfNotBlank()?.let { userName.text = it }
            user.email?.takeIfNotBlank()?.let { userEmail.text = it }
        }

        PreferenceManager.getDefaultSharedPreferences(this)
            .getString(KEY_CURRENT_FARM_NAME, null)?.takeIfNotBlank()?.let {
                farmName.text = it
            }
    }

    private fun setupNavigationDrawer(navController: NavController) {
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
        NavigationUI.setupWithNavController(nav_view, navController)
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
