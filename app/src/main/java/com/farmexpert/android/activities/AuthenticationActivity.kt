/*
 * Developed by Lucian Iacob.
 * Cluj-Napoca, 2019.
 * Project: FarmExpert
 * Email: contact@lucianiacob.com
 * Last modified 3/10/19 3:32 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import com.farmexpert.android.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.activity_authentication.*
import org.jetbrains.anko.startActivity

class AuthenticationActivity : Activity() {

    companion object {
        const val RC_SIGN_IN = 1234
    }

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
            .setIsSmartLockEnabled(true, true)
            .setAlwaysShowSignInMethodScreen(true)
            .setTosAndPrivacyPolicyUrls("https://www.lucianiacob.com", "https://www.lucianiacob.com")
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
            if (resultCode == Activity.RESULT_OK) {
                startActivity<FarmSelectorActivity>()
                finish()
            } else {
                val response = IdpResponse.fromResultIntent(data)
                if (response?.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    showMessage(R.string.no_internet_connection)
                    return
                }

                if (response?.error?.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    showMessage(R.string.auth_unknown_error)
                    return
                }
            }
        }
    }

    private fun showMessage(@StringRes stringId: Int) {
        Toast.makeText(this, stringId, Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        signInButton.setOnClickListener(null)
    }
}
