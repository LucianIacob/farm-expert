/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/21/19 3:37 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.farmexpert.android.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = host.navController

        setupNavigationDrawer(navController)
    }

    private fun setupNavigationDrawer(navController: NavController) {
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
        NavigationUI.setupWithNavController(nav_view, navController)
    }

    override fun onResume() {
        super.onResume()
        val header = nav_view.getHeaderView(0)
        header.findViewById<View>(R.id.developer_mode).setOnTouchListener(devModeListener)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(this, R.id.nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawer_layout)
    }

    private val devModeListener: View.OnTouchListener = object : View.OnTouchListener {
        var handler = Handler()
        var numberOfTaps = 0
        var lastTapTimeMs: Long = 0
        var touchDownMs: Long = 0

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> touchDownMs = System.currentTimeMillis()
                MotionEvent.ACTION_UP -> {
                    handler.removeCallbacksAndMessages(null)

                    if (System.currentTimeMillis() - touchDownMs > ViewConfiguration.getTapTimeout()) {
                        numberOfTaps = 0
                        lastTapTimeMs = 0
                        return true
                    }

                    if (numberOfTaps > 0 && System.currentTimeMillis() - lastTapTimeMs < ViewConfiguration.getDoubleTapTimeout()) {
                        numberOfTaps += 1
                    } else {
                        numberOfTaps = 1
                    }

                    lastTapTimeMs = System.currentTimeMillis()

                    if (numberOfTaps == 5) {
//                        val devMode = AppUtils.setDevMode()
//                        Toast.makeText(applicationContext, "Developer Mode status: $devMode", Toast.LENGTH_SHORT).show()
//                        Answers.getInstance().logLogin(LoginEvent().putMethod("DEV_MODE").putSuccess(devMode))
                    }
                }
            }
            return true
        }
    }
}
