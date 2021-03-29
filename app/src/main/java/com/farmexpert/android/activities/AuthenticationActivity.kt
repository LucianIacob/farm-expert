/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 10/23/19 12:26 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.farmexpert.android.BuildConfig
import com.farmexpert.android.R
import com.farmexpert.android.utils.error
import com.farmexpert.android.utils.startActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.activity_authentication.*

class AuthenticationActivity : AppCompatActivity(R.layout.activity_authentication) {

    override fun onStart() {
        super.onStart()
        signInButton.setOnClickListener { startSignIn() }
    }

    private fun startSignIn() {
        val intent = AuthUI.getInstance().createSignInIntentBuilder()
            .setTheme(R.style.AppTheme)
            .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
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
                    error(IdpResponse.fromResultIntent(data)?.error)
                    Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    companion object {
        const val RC_SIGN_IN = 1234
        const val termsOfServiceUrl = "https://lucianiacob.com/farmexpert/tos.html"
        const val privacyPolicyUrl = "https://lucianiacob.com/farmexpert/privacypolicy.html"
    }
}
