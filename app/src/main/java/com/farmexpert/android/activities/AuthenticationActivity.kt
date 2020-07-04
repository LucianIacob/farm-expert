/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 10/23/19 12:26 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.farmexpert.android.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.activity_authentication.*
import org.jetbrains.anko.startActivity

class AuthenticationActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
    }

    override fun onStart() {
        super.onStart()
        signInButton.setOnClickListener { startSignIn() }
    }

    private fun startSignIn() {
        val intent = AuthUI.getInstance().createSignInIntentBuilder()
            .setTheme(R.style.AppTheme)
            .setIsSmartLockEnabled(true, true)
            .setAlwaysShowSignInMethodScreen(true)
            .setTosAndPrivacyPolicyUrls(termsOfServiceUrl, privacyPolicyUrl)
            .setAvailableProviders(
                listOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.PhoneBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build()
                )
            )
            .setLogo(R.drawable.icon_launcher)
            .build()

        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                startActivity<FarmSelectorActivity>()
                finish()
            } else {
                IdpResponse.fromResultIntent(data)?.error?.message?.let {
                    Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun showMessage(message: String) {
    }

    companion object {
        const val RC_SIGN_IN = 1234
        const val termsOfServiceUrl = "http://lucianiacob.com/farmexpert/tos.html"
        const val privacyPolicyUrl = "http://lucianiacob.com/farmexpert/privacypolicy.html"
    }
}
