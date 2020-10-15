package com.farmexpert.android.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.farmexpert.android.R
import com.farmexpert.android.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_change_email.*

class ChangeUserEmailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email)

        setupToolbar()

        intent.extras?.apply {
            getString(USER_EMAIL)?.let {
                updateBtn.text = getString(R.string.update)
                userEmailBox.setText(it)
                it.isValidInput()?.let { updateBtn.isEnabled = true }
            } ?: run {
                updateBtn.text = getString(R.string.set)
                emailVerifiedSeparator.visibility = View.GONE
                sendVerificationEmailBtn.visibility = View.GONE
            }

            getBoolean(EMAIL_VERIFIED).takeIfTrue()?.let {
                emailVerifiedSeparator.visibility = View.GONE
                sendVerificationEmailBtn.visibility = View.GONE
            }
        }

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(
            intent.extras?.getString(USER_EMAIL)?.let { R.string.update_email_title }
                ?: R.string.set_email_title
        )
    }

    override fun onResume() {
        super.onResume()
        userEmailBox.doOnTextChanged { text, _, _, _ ->
            updateBtn.isEnabled = text?.isValidInput()?.let { true } ?: false
        }

        updateBtn.setOnClickListener { updateUserEmail() }
        sendVerificationEmailBtn.setOnClickListener { _ ->
            FirebaseAuth.getInstance().currentUser?.let { it -> sendUserEmailVerification(it) }
        }
    }

    private fun updateUserEmail() {
        FirebaseAuth.getInstance().useAppLanguage()
        FirebaseAuth.getInstance().currentUser?.apply {
            loadingView.visibility = View.VISIBLE

            updateEmail(userEmailBox.text.toString())
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
                .addOnCompleteListener { loadingView.visibility = View.INVISIBLE }
        }
    }

    private fun sendUserEmailVerification(firebaseUser: FirebaseUser) {
        firebaseUser
            .sendEmailVerification()
            .addOnSuccessListener { toast(R.string.email_verification_success) }
            .addLoggableFailureListener()
            .addOnCompleteListener {
                info { "email verification link sent to ${userEmailBox.text.toString()}" }
            }
    }

    companion object {
        const val USER_EMAIL = "com.farmexpert.android.UserEmail"
        const val EMAIL_VERIFIED = "com.farmexpert.android.EmailVerified"
    }
}