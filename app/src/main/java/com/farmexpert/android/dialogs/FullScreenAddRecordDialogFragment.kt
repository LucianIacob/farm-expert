package com.farmexpert.android.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.farmexpert.android.R
import kotlinx.android.synthetic.main.fullscreen_dialog.*

abstract class FullScreenAddRecordDialogFragment : BaseAddRecordDialogFragment() {

    abstract val dialogTitle: Int

    abstract val dialogContent: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = View.inflate(activity, R.layout.fullscreen_dialog, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        setupViewStub()
        setupToolbar()
        onUiElementsReady()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.setLayout(width, height)
            it.setWindowAnimations(R.style.AppTheme_Slide)
        }
    }

    abstract fun addRecordClicked()

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.title = getString(dialogTitle)
        toolbar.setOnMenuItemClickListener {
            addRecordClicked()
            true
        }
    }

    private fun setupViewStub() {
        fullScreenContent.layoutResource = dialogContent
        fullScreenContent.inflate()
    }
}