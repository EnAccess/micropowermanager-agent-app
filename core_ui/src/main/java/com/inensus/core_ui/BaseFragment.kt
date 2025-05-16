package com.inensus.core_ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

abstract class BaseFragment : Fragment() {
    abstract fun provideViewModel(): BaseViewModel

    private var progressDialog: AlertDialog? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        observeLoading()
        observeError()
    }

    private fun observeLoading() {
        provideViewModel().loading.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    showLoading()
                } else {
                    hideLoading()
                }
            },
        )
    }

    private fun observeError() {
        provideViewModel().error.observe(
            viewLifecycleOwner,
            Observer {
                showAlertDialog(context, it.data.message[0])
            },
        )
    }

    private fun showAlertDialog(
        context: Context?,
        message: String?,
    ): AlertDialog? =
        context?.let {
            AlertDialog
                .Builder(context)
                .setTitle(getString(R.string.error))
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok)) { _, _ -> }
                .show()
        }

    private fun showLoading() {
        if (!requireActivity().isFinishing) {
            if (progressDialog == null) {
                initializeLoading(requireActivity())
            }
            progressDialog?.show()
        }
    }

    private fun hideLoading() {
        if (!requireActivity().isFinishing) {
            progressDialog?.cancel()
            progressDialog = null
        }
    }

    private fun initializeLoading(activity: Activity) {
        if (!activity.isFinishing) {
            val builder = AlertDialog.Builder(activity)
            builder.setCancelable(false)
            builder.setView(R.layout.loading_dialog)
            progressDialog = builder.create()
        }
    }
}
