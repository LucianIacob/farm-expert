package com.farmexpert.android.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.farmexpert.android.R
import com.farmexpert.android.utils.addLoggableFailureListener
import com.farmexpert.android.utils.alert
import com.farmexpert.android.utils.isValidInput
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_change_username.*

class ChangeUserNameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_username)

        setupToolbar()
        intent.extras?.getString(USER_NAME)?.let {
            userNameBox.setText(it)
            it.isValidInput()?.let { updateBtn.isEnabled = true }
        }
        userNameBox.requestFocus()
        (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.showSoftInput(userNameBox, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(
            intent.extras?.getString(USER_NAME)?.let { R.string.update_username_title }
                ?: R.string.set_username_title
        )
    }

    override fun onResume() {
        super.onResume()
        userNameBox.doOnTextChanged { text, _, _, _ ->
            updateBtn.isEnabled = text?.isValidInput()?.let { true } ?: false
        }

        updateBtn.setOnClickListener { updateUserName() }
    }

    private fun updateUserName() {
        val changeRequest = UserProfileChangeRequest.Builder()
            .setDisplayName(userNameBox.text.toString())
            .build()

        loadingView.visibility = View.VISIBLE
        FirebaseAuth.getInstance().currentUser
            ?.updateProfile(changeRequest)
            ?.addOnSuccessListener { finish() }
            ?.addLoggableFailureListener { exception ->
                exception.message?.let { alert(it) }
            }
            ?.addOnCompleteListener { loadingView.visibility = View.INVISIBLE }
    }

    companion object {
        const val USER_NAME = "com.farmexpert.android.UserName"
    }
}