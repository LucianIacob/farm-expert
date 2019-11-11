package com.farmexpert.android.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.farmexpert.android.R
import kotlinx.android.synthetic.main.activity_change_username.*
import org.jetbrains.anko.AnkoLogger

class ChangeUserNameActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_username)

        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(
            intent.extras?.getString(USER_NAME)?.let { R.string.update_username_title }
                ?: R.string.set_username_title
        )
    }

    companion object {
        const val USER_NAME = "com.farmexpert.android.UserName"
    }
}
