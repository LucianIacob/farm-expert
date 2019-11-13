package com.farmexpert.android.activities

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.View.*
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.preference.PreferenceManager
import com.crashlytics.android.Crashlytics
import com.farmexpert.android.R
import com.farmexpert.android.model.Farm
import com.farmexpert.android.utils.*
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_profile.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*

class UserProfileActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().currentUser?.run {
            loadingView.visibility = VISIBLE
            reload()
                .addOnSuccessListener { fillData(this) }
                .addOnFailureListener {
                    error { it }
                    alert(getString(R.string.profile_load_unsuccessful, it.message))
                }
                .addOnCompleteListener { loadingView.visibility = INVISIBLE }
        } ?: run {
            alert(
                message = R.string.user_not_available,
                isCancellable = false,
                okListener = { finish() }
            )
        }
    }

    private fun fillData(firebaseUser: FirebaseUser) {
        with(firebaseUser) {
            photoUrl?.toString()?.isNotEmpty()?.let {
                Picasso.get()
                    .load(photoUrl)
                    .transform(CircleTransform())
                    .fit()
                    .centerCrop()
                    .into(icon)
            }

            metadata?.creationTimestamp?.let {
                createdOn.text = getString(R.string.user_creation_date, Date(it).getShort())
            } ?: run { createdOn.visibility = GONE }

            providerData.lastOrNull()?.providerId?.takeIfNotBlank()?.let {
                val providerText = it.mapProviderId(resources)
                provider.text = getString(R.string.profile_created_with_provider, providerText)
            } ?: run { provider.visibility = GONE }

            displayName?.takeIfNotBlank()?.let {
                name.setTextColor(getTextColor(R.color.black))
                name.text = it
            } ?: run {
                name.text = getString(R.string.add_name)
                name.setTextColor(getTextColor(R.color.link_color))
            }

            phoneNumber?.takeIfNotBlank()?.let { phone.text = it } ?: run {
                phoneInfoGroup.visibility = GONE
                phoneSeparator.visibility = GONE
            }

            email?.takeIfNotBlank()?.let {
                userEmail.text = it
                userEmail.setTextColor(getTextColor(R.color.black))
                displayEmailVerificationStatus(isEmailVerified)
                handlePasswordResetClick(it)
            } ?: run {
                userEmail.text = getString(R.string.add_email)
                userEmail.setTextColor(getTextColor(R.color.link_color))
                passResetGroup.visibility = GONE
                passResetSeparator.visibility = GONE
            }


//            user.updatePassword()
//

            fetchSubscribedFarms(this)
            setClickListeners(this)
        }
    }

    private fun handlePasswordResetClick(emailAddress: String) {
        passReset.setText(R.string.pass_reset_message)
        passResetGroup.setOnClickListener {
            loadingView.visibility = VISIBLE
            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(emailAddress)
                .addOnSuccessListener { alert(R.string.password_reset_success) }
                .addOnFailureListener {
                    it.message?.let { it1 -> alert(message = it1, isCancellable = true) }
                }
                .addOnCompleteListener { loadingView.visibility = INVISIBLE }
        }
    }

    private fun setClickListeners(firebaseUser: FirebaseUser) {
        editAccountPicture.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_RC
            )
        }

        nameInfoGroup.setOnClickListener {
            startActivity<ChangeUserNameActivity>(
                ChangeUserNameActivity.USER_NAME to firebaseUser.displayName
            )
        }

        emailInfoGroup.setOnClickListener {
            startActivity<ChangeUserEmailActivity>(
                ChangeUserEmailActivity.USER_EMAIL to firebaseUser.email,
                ChangeUserEmailActivity.EMAIL_VERIFIED to firebaseUser.isEmailVerified
            )
        }

        deleteAccountGroup.setOnClickListener {
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
        loadingView.visibility = VISIBLE

        firebaseUser
            .delete()
            .addOnSuccessListener {
                PreferenceManager.getDefaultSharedPreferences(this)
                    .edit { remove(FarmSelectorActivity.KEY_CURRENT_FARM_ID) }
                startActivity<AuthenticationActivity>()
                finishAffinity()
            }
            .addOnFailureListener { it.message?.let { it1 -> alert(it1) } }
            .addOnCompleteListener { loadingView?.visibility = VISIBLE }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PICK_IMAGE_RC -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let { uploadProfilePicture(it) }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun uploadProfilePicture(imageUri: Uri) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            val metadata = storageMetadata { contentType = "image/jpg" }

            loadingView.visibility = VISIBLE
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
                .addOnSuccessListener { updateUserProfile(user, it) }
                .addOnFailureListener {
                    loadingView.visibility = INVISIBLE
                    alert(R.string.err_updating_record)
                    error { it }
                }
        }
    }

    private fun updateUserProfile(user: FirebaseUser, imageUri: Uri) {
        user
            .updateProfile(
                UserProfileChangeRequest.Builder()
                    .setPhotoUri(imageUri)
                    .build()
            )
            .addOnSuccessListener {
                user.reload().addOnSuccessListener {
                    fillData(user)
                    loadingView.visibility = INVISIBLE
                }
            }
            .addOnFailureListener { exception ->
                exception.message?.let { toast(it) }
                loadingView.visibility = INVISIBLE
            }
    }

    private fun fetchSubscribedFarms(firebaseUser: FirebaseUser) {
        farmsList.text = getString(R.string.fui_progress_dialog_loading)
        Firebase.firestore
            .collection(FirestorePath.Collections.FARMS)
            .whereArrayContains(FirestorePath.Farm.USERS, firebaseUser.uid)
            .get()
            .addOnSuccessListener { snapshots ->
                val subscribedFarms = snapshots.toObjects<Farm>()
                farmsList.text = subscribedFarms.joinToString(
                    separator = "\n",
                    transform = { it.name }
                )
            }
            .addOnFailureListener {
                error { it }
                Crashlytics.logException(it)
                farmsList.text = getString(R.string.err_retrieving_farms)
            }
    }

    private fun displayEmailVerificationStatus(emailVerified: Boolean) {
        val tintColor = if (emailVerified) R.color.sea_green else android.R.color.darker_gray
        ContextCompat.getDrawable(this, R.drawable.baseline_verified_user_black_18)?.let {
            val wrappedDrawable = DrawableCompat.wrap(it)
            DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(this, tintColor))
            userEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, wrappedDrawable, null)
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
