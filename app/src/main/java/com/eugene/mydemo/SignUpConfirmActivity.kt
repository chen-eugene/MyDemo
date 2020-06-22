package com.eugene.mydemo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.amplifyframework.core.Amplify
import com.eugene.mydemo.app.BaseActivity
import kotlinx.android.synthetic.main.activity_confirm.*

class SignUpConfirmActivity : BaseActivity() {

    private var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)
        toolbar.setTitle("")
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener(View.OnClickListener { onBackPressed() })

        userName = intent.getStringExtra("username") ?: ""
        editTextConfirmUserId.setText(userName)

        initView()
    }

    private fun initView() {
        editTextConfirmUserId.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 0) {
                    textViewConfirmUserIdLabel.setText(R.string.Username)
                    editTextConfirmUserId.setBackgroundResource(R.drawable.text_border_selector)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textViewConfirmUserIdMessage.text = ""
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0) {
                    textViewConfirmUserIdLabel.text = ""
                }
            }

        })

        editTextConfirmCode.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 0) {
                    textViewConfirmCodeLabel.setText(R.string.Username)
                    editTextConfirmCode.setBackgroundResource(R.drawable.text_border_selector)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textViewConfirmCodeMessage.text = ""
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0) {
                    textViewConfirmCodeLabel.text = ""
                }
            }

        })

        confirm_button.setOnClickListener {
            confirm()
        }
    }

    private fun confirm() {
        userName = editTextConfirmUserId.text.toString()
        if (userName.isNullOrBlank()) {
            editTextConfirmUserId.setBackgroundResource(R.drawable.text_border_error)
            return
        }

        val code = editTextConfirmCode.text.toString()
        if (code.isNullOrBlank()) {
            editTextConfirmCode.setBackgroundResource(R.drawable.text_border_error)
            return
        }

        showWaitDialog("confirm ...")
        Amplify.Auth.confirmSignUp(
            userName,
            code,
            { result ->
                closeWaitDialog()
                showDialogMessage("Success", "$userName  has been confirmed!") {
                    exit()
                }
            },
            { error ->
                closeWaitDialog()
                showDialogMessage("Success", "$userName  has been confirmed!",null)
            }
        )
    }

    private fun exit() {
        val intent = Intent()
        intent.putExtra("name", userName)
        setResult(RESULT_OK, intent)
        finish()
    }

}