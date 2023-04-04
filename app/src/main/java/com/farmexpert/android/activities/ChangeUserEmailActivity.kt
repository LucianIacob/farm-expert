/*
 * Developed by Lucian Iacob.
 * Romania, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:38 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.farmexpert.android.R
import com.farmexpert.android.databinding.ActivityChangeEmailBinding
import com.farmexpert.android.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ChangeUserEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()

        intent.extras?.apply {
            getString(USER_EMAIL)?.let {
                binding.updateBtn.text = getString(R.string.update)
                binding.userEmailBox.setText(it)
                it.isValidInput()?.let { binding.updateBtn.isEnabled = true }
            } ?: run {
                binding.updateBtn.text = getString(R.string.set)
                binding.emailVerifiedSeparator.isVisible = false
                binding.sendVerificationEmailBtn.isVisible = false
            }

            binding.emailVerifiedSeparator.isVisible = getBoolean(EMAIL_VERIFIED, false)
            binding.sendVerificationEmailBtn.isVisible = getBoolean(EMAIL_VERIFIED, false)
        }

    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(
            intent.extras?.getString(USER_EMAIL)?.let { R.string.update_email_title }
                ?: R.string.set_email_title
        )
    }

    override fun onResume() {
        super.onResume()
        binding.userEmailBox.doOnTextChanged { text, _, _, _ ->
            binding.updateBtn.isEnabled = text?.isValidInput()?.let { true } ?: false
        }

        binding.updateBtn.setOnClickListener { updateUserEmail() }
        binding.sendVerificationEmailBtn.setOnClickListener { _ ->
            FirebaseAuth.getInstance().currentUser?.let { it -> sendUserEmailVerification(it) }
        }
    }

    private fun updateUserEmail() {
        FirebaseAuth.getInstance().useAppLanguage()
        FirebaseAuth.getInstance().currentUser?.apply {
            binding.loadingView.isInvisible = false

            updateEmail(binding.userEmailBox.text.toString())
                .addOnSuccessListener {
                    sendUserEmailVerification(firebaseUser = this)
                    alert(
                        message = getString(R.string.after_email_change_message),
                        isCancellable = false,
                        okListener = { finish() }
                    )
                }
                .addLoggableFailureListener { exception ->
                    exception.message?.let { alert(message = it, isCancellable = true) }
                }
                .addOnCompleteListener { binding.loadingView.isInvisible = true }
        }
    }

    private fun sendUserEmailVerification(firebaseUser: FirebaseUser) {
        firebaseUser
            .sendEmailVerification()
            .addOnSuccessListener { toast(R.string.email_verification_success) }
            .addLoggableFailureListener()
            .addOnCompleteListener {
                info { "email verification link sent to ${binding.userEmailBox.text.toString()}" }
            }
    }

    companion object {
        const val USER_EMAIL = "com.farmexpert.android.UserEmail"
        const val EMAIL_VERIFIED = "com.farmexpert.android.EmailVerified"
    }
}