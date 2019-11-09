package com.farmexpert.android.activities

import android.content.res.Resources
import android.os.Bundle
import android.view.View.GONE
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.crashlytics.android.Crashlytics
import com.farmexpert.android.R
import com.farmexpert.android.model.Farm
import com.farmexpert.android.utils.*
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_profile.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import java.util.*

class UserProfileActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        setupToolbar()
        fillData()
    }

    private fun fillData() {
        FirebaseAuth.getInstance().currentUser?.apply {
            photoUrl?.toString()?.isNotEmpty()?.let {
                Picasso.get().load(photoUrl).transform(CircleTransform()).into(icon)
            }

            metadata?.creationTimestamp?.let {
                createdOn.text = getString(R.string.user_creation_date, Date(it).getShort())
            } ?: run { createdOn.visibility = GONE }

            providerData.lastOrNull()?.providerId?.takeIfNotBlank()?.let {
                val providerText = it.mapProviderId(resources)
                provider.text = getString(R.string.profile_created_with_provider, providerText)
            } ?: run { provider.visibility = GONE }

            //            user.reload() // todo in on resume maybe
            displayEmailVerificationStatus(isEmailVerified)

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

            email?.takeIfNotBlank()?.let { userEmail.text = it } ?: run {
                emailInfoGroup.visibility = GONE
                emailSeparator.visibility = GONE
            }

//            user.sendEmailVerification(
//                ActionCodeSettings.newBuilder()
//                    .setAndroidPackageName("", true, "1.0.0")
//                    .build()
//            )

//            updateEmail()
//            user.updatePassword()
//
//            user.updateProfile(
//                UserProfileChangeRequest.Builder()
//                    .setDisplayName()
//                    .setPhotoUri()
//                    .build()
//            )
            fetchSubscribedFarms(this)
        } ?: run {
            failureAlert(
                message = R.string.user_not_available,
                isCancellable = false,
                okListener = { finish() }
            )
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

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getTextColor(@ColorRes colorId: Int) =
        ResourcesCompat.getColor(resources, colorId, null)
}

private fun String.mapProviderId(resources: Resources): String = when (this) {
    EmailAuthProvider.PROVIDER_ID -> resources.getString(R.string.email_provider)
    GoogleAuthProvider.PROVIDER_ID -> resources.getString(R.string.google_provider)
    PhoneAuthProvider.PROVIDER_ID -> resources.getString(R.string.phone_provider)
    else -> resources.getString(R.string.unknown_provider)
}
