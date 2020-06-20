package com.eugene.mydemo.app

import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    private var waitDialog: ProgressDialog? = null

    private var userDialog: AlertDialog? = null

    protected fun showWaitDialog(message: String) {
        closeWaitDialog()
        waitDialog = ProgressDialog(this)
        waitDialog?.setTitle(message)
        waitDialog?.show()
    }

    protected fun closeWaitDialog() {
        try {
            waitDialog?.dismiss()
        } catch (e: Exception) {
            //
        }
    }

    protected fun showDialogMessage(title: String, body: String, callback: (() -> Unit)?) {
        runOnUiThread {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(title).setMessage(body)
                .setNeutralButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    try {
                        userDialog?.dismiss()
                        callback?.invoke()
                    } catch (e: java.lang.Exception) {
                    }
                })
            userDialog = builder.create()
            userDialog?.show()
        }
    }


}