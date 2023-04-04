/*
 * Developed by Lucian Iacob.
 * Romania, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:38 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.farmexpert.android.BuildConfig
import com.farmexpert.android.R
import com.farmexpert.android.databinding.ActivityAuthenticationBinding
import com.farmexpert.android.utils.error
import com.farmexpert.android.utils.startActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding

    override fun onStart() {
        super.onStart()
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signInButton.setOnClickListener { startSignIn() }
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

    @Deprecated("Deprecated in Java")
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
        const val termsOfServiceUrl = "https://doc-hosting.flycricket.io/farmexpert-terms-of-use/6acdfcce-619f-4a24-add4-7bbcb8526070/terms"
        const val privacyPolicyUrl = "https://doc-hosting.flycricket.io/farmexpert-privacy-policy/4eb4e5b6-8d73-4bf0-b4ae-03a7e90b9415/privacy"
    }
}
