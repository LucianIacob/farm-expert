/*
 * Developed by Lucian Iacob.
 * Romania, 2023.
 * Project: FarmExpert
 * Email: lucian@iacob.email
 * Last modified 4/4/23, 1:52 PM.
 * Copyright (c) Lucian Iacob. All rights reserved.
 */

package com.farmexpert.android.activities

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.farmexpert.android.BuildConfig
import com.farmexpert.android.R
import com.farmexpert.android.databinding.ActivityUserProfileBinding
import com.farmexpert.android.model.Farm
import com.farmexpert.android.utils.*
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.squareup.picasso.Picasso
import java.util.*

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().currentUser?.run {
            binding.loadingView.isInvisible = false
            reload()
                .addOnSuccessListener { fillData(firebaseUser = this) }
                .addLoggableFailureListener {
                    alert(getString(R.string.profile_load_unsuccessful, it.message))
                }
                .addOnCompleteListener { binding.loadingView.isInvisible = true }
        } ?: run {
            alert(
                message = R.string.user_not_available,
                isCancellable = false,
                okListener = { finish() }
            )
        }

        setAppVersion()
    }

    private fun fillData(firebaseUser: FirebaseUser) = with(firebaseUser) {
        photoUrl?.toString()?.isNotEmpty()?.let {
            Picasso.get()
                .load(photoUrl)
                .transform(CircleTransform())
                .fit()
                .centerCrop()
                .into(binding.icon)
        }

        metadata?.creationTimestamp?.let {
            binding.createdOn.text = getString(R.string.user_creation_date, Date(it).getShort())
        } ?: run { binding.createdOn.isVisible = false }

        providerData.lastOrNull()?.providerId?.takeIfNotBlank()?.let {
            val providerText = it.mapProviderId(resources)
            binding.provider.text = getString(R.string.profile_created_with_provider, providerText)
        } ?: run { binding.provider.isVisible = false }

        displayName?.takeIfNotBlank()?.let {
            binding.name.setTextColor(getTextColor(R.color.black))
            binding.name.text = it
        } ?: run {
            binding.name.text = getString(R.string.add_name)
            binding.name.setTextColor(getTextColor(R.color.link_color))
        }

        phoneNumber?.takeIfNotBlank()?.let { binding.phone.text = it } ?: run {
            binding.phoneInfoGroup.isVisible = false
            binding.phoneSeparator.isVisible = false
        }

        email?.takeIfNotBlank()?.let {
            binding.userEmail.text = it
            binding.userEmail.setTextColor(getTextColor(R.color.black))
            displayEmailVerificationStatus(isEmailVerified)
            handlePasswordResetClick(it)
        } ?: run {
            binding.userEmail.text = getString(R.string.add_email)
            binding.userEmail.setTextColor(getTextColor(R.color.link_color))
            binding.passResetGroup.isVisible = false
            binding.passResetSeparator.isVisible = false
        }

        fetchSubscribedFarms(firebaseUser = this)
        setClickListeners(this)
    }

    private fun setAppVersion() {
        binding.appVersionHeader.text =
            getString(R.string.app_version_header, getString(R.string.app_name))
        val sb = StringBuilder()
            .append(BuildConfig.BUILD_TYPE)
            .append(" ")
            .append(BuildConfig.VERSION_NAME)
        binding.appVersion.text = sb.toString()
    }

    private fun handlePasswordResetClick(emailAddress: String) {
        binding.passReset.setText(R.string.pass_reset_message)
        binding.passResetGroup.setOnClickListener {
            binding.loadingView.isInvisible = false
            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(emailAddress)
                .addOnSuccessListener { alert(R.string.password_reset_success) }
                .addLoggableFailureListener {
                    it.message?.let { message -> alert(message = message, isCancellable = true) }
                }
                .addOnCompleteListener { binding.loadingView.isInvisible = true }
        }
    }

    private fun setClickListeners(firebaseUser: FirebaseUser) {
        binding.editAccountPicture.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_RC
            )
        }

        binding.nameInfoGroup.setOnClickListener {
            startActivity<ChangeUserNameActivity>(
                ChangeUserNameActivity.USER_NAME to firebaseUser.displayName
            )
        }

        binding.emailInfoGroup.setOnClickListener {
            startActivity<ChangeUserEmailActivity>(
                ChangeUserEmailActivity.USER_EMAIL to firebaseUser.email,
                ChangeUserEmailActivity.EMAIL_VERIFIED to firebaseUser.isEmailVerified
            )
        }

        binding.deleteAccountGroup.setOnClickListener {
            alert(
                message = R.string.delete_account_confirmation,
                negativeButton = true,
                isCancellable = true,
                okListener = { deleteUserAccount(firebaseUser) },
                redButton = true
            )
        }
    }

    private fun deleteUserAccount(firebaseUser: FirebaseUser) {
        binding.loadingView.isInvisible = false

        Firebase.firestore
            .collection(FirestorePath.Collections.FARMS)
            .whereArrayContains(FirestorePath.Farm.USERS, firebaseUser.uid)
            .get()
            .addOnSuccessListener { snapshots ->
                info { "removing subscriber from ${snapshots.size()} farms" }
                Firebase.firestore
                    .runBatch {
                        snapshots
                            .map { it.toObject<Farm>().apply { id = it.id } }
                            .mapNotNull { it.id }
                            .map { farmId ->
                                info { "updating farm with id $farmId" }
                                it.update(
                                    Firebase.firestore
                                        .collection(FirestorePath.Collections.FARMS)
                                        .document(farmId),
                                    FirestorePath.Farm.USERS,
                                    FieldValue.arrayRemove(firebaseUser.uid)
                                )
                            }

                    }
                    .addOnSuccessListener {
                        info { "batch success" }
                        finallyDeleteUser()
                    }
                    .addLoggableFailureListener {
                        it.message?.let { message -> alert(message) }
                        binding.loadingView.isInvisible = true
                    }

            }
            .addLoggableFailureListener {
                it.message?.let { message -> alert(message) }
                binding.loadingView.isInvisible = true
            }
    }

    private fun finallyDeleteUser() {
        AuthUI.getInstance()
            .delete(this)
            .addOnSuccessListener {
                info { "delete user success" }
                PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .edit {
                        remove(FarmSelectorActivity.KEY_CURRENT_FARM_ID)
                    }

                startActivity<AuthenticationActivity>()
                finishAffinity()
            }
            .addLoggableFailureListener {
                it.message?.let { message -> alert(message) }
            }
            .addOnCompleteListener { binding.loadingView.isInvisible = true }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PICK_IMAGE_RC -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let { uploadProfilePicture(imageUri = it) }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun uploadProfilePicture(imageUri: Uri) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            val metadata = storageMetadata { contentType = "image/jpg" }

            binding.loadingView.isInvisible = false
            val fileRef = Firebase.storage
                .getReference("profilepictures")
                .child(user.uid)

            fileRef.putFile(imageUri, metadata)
                .continueWithTask { task ->
                    if (task.isSuccessful.not()) {
                        task.exception?.let { throw it }
                    }
                    fileRef.downloadUrl
                }
                .addOnSuccessListener { uri ->
                    updateUserProfile(
                        firebaseUser = user,
                        imageUri = uri
                    )
                }
                .addLoggableFailureListener {
                    binding.loadingView.isInvisible = true
                    alert(R.string.err_updating_record)
                }
        }
    }

    private fun updateUserProfile(firebaseUser: FirebaseUser, imageUri: Uri) {
        firebaseUser
            .updateProfile(
                UserProfileChangeRequest.Builder()
                    .setPhotoUri(imageUri)
                    .build()
            )
            .addOnSuccessListener {
                firebaseUser.reload().addOnSuccessListener {
                    fillData(firebaseUser = firebaseUser)
                    binding.loadingView.isInvisible = true
                }
            }
            .addLoggableFailureListener { exception ->
                exception.message?.let { toast(it) }
                binding.loadingView.isInvisible = true
            }
    }

    private fun fetchSubscribedFarms(firebaseUser: FirebaseUser) {
        binding.farmsList.text = getString(R.string.fui_progress_dialog_loading)
        Firebase.firestore
            .collection(FirestorePath.Collections.FARMS)
            .whereArrayContains(FirestorePath.Farm.USERS, firebaseUser.uid)
            .get()
            .addOnSuccessListener { snapshots ->
                val subscribedFarms = snapshots.toObjects<Farm>()
                binding.farmsList.text = subscribedFarms.joinToString(
                    separator = "\n",
                    transform = { it.name }
                )
                binding.farmsInfoGroup.setOnClickListener { startActivity<UserFarmsActivity>() }
            }
            .addLoggableFailureListener {
                binding.farmsList.text = getString(R.string.err_retrieving_farms)
            }
    }

    private fun displayEmailVerificationStatus(emailVerified: Boolean) {
        val tintColor = if (emailVerified) R.color.sea_green else android.R.color.darker_gray
        ContextCompat.getDrawable(this, R.drawable.baseline_verified_user_black_18)?.let {
            val wrappedDrawable = DrawableCompat.wrap(it)
            DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(this, tintColor))
            binding.userEmail.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                wrappedDrawable,
                null
            )
        }
    }

    private fun getTextColor(@ColorRes colorId: Int) =
        ResourcesCompat.getColor(resources, colorId, null)

    companion object {
        const val PICK_IMAGE_RC = 1234
    }
}

private fun String.mapProviderId(resources: Resources): String = when (this) {
    EmailAuthProvider.PROVIDER_ID -> resources.getString(R.string.email_provider)
    GoogleAuthProvider.PROVIDER_ID -> resources.getString(R.string.google_provider)
    PhoneAuthProvider.PROVIDER_ID -> resources.getString(R.string.phone_provider)
    else -> resources.getString(R.string.unknown_provider)
}
