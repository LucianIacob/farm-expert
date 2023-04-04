/*
 * Developed by Lucian Iacob.
 * Romania, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:42 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.widget.doOnTextChanged
import com.farmexpert.android.R
import com.farmexpert.android.databinding.ActivityChangeUsernameBinding
import com.farmexpert.android.utils.addLoggableFailureListener
import com.farmexpert.android.utils.alert
import com.farmexpert.android.utils.isValidInput
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class ChangeUserNameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeUsernameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        intent.extras?.getString(USER_NAME)?.let {
            binding.userNameBox.setText(it)
            it.isValidInput()?.let { binding.updateBtn.isEnabled = true }
        }
        binding.userNameBox.requestFocus()
        (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.showSoftInput(binding.userNameBox, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(
            intent.extras?.getString(USER_NAME)?.let { R.string.update_username_title }
                ?: R.string.set_username_title
        )
    }

    override fun onResume() {
        super.onResume()
        binding.userNameBox.doOnTextChanged { text, _, _, _ ->
            binding.updateBtn.isEnabled = text?.isValidInput()?.let { true } ?: false
        }

        binding.updateBtn.setOnClickListener { updateUserName() }
    }

    private fun updateUserName() {
        val changeRequest = UserProfileChangeRequest.Builder()
            .setDisplayName(binding.userNameBox.text.toString())
            .build()

        binding.loadingView.isInvisible = false
        FirebaseAuth.getInstance().currentUser
            ?.updateProfile(changeRequest)
            ?.addOnSuccessListener { finish() }
            ?.addLoggableFailureListener { exception ->
                exception.message?.let { alert(it) }
            }
            ?.addOnCompleteListener { binding.loadingView.isInvisible = true }
    }

    companion object {
        const val USER_NAME = "com.farmexpert.android.UserName"
    }
}