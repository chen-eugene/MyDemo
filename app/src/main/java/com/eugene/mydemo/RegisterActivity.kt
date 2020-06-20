package com.eugene.mydemo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.eugene.mydemo.app.BaseActivity
import com.eugene.mydemo.utils.start
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    private var username: String = ""
    private var pwd: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
    }

    private fun initView() {
        toolbar_Register.setTitle("")
        setSupportActionBar(toolbar_Register)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar_Register.setNavigationOnClickListener {
            onBackPressed()
        }

        editTextRegUserId.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 0) {
                    textViewRegUserIdLabel.setText(R.string.Username)
                    editTextRegUserId.setBackgroundResource(R.drawable.text_border_selector)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textViewRegUserIdMessage.text = ""
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0) {
                    textViewRegUserIdLabel.text = ""
                }
            }

        })

        editTextRegUserPassword.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 0) {
                    textViewRegUserPasswordLabel.setText(R.string.Username)
                    editTextRegUserPassword.setBackgroundResource(R.drawable.text_border_selector)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textViewUserRegPasswordMessage.text = ""
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0) {
                    textViewRegUserPasswordLabel.text = ""
                }
            }

        })

        editTextRegEmail.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 0) {
                    textViewRegEmailLabel.setText(R.string.Username)
                    editTextRegEmail.setBackgroundResource(R.drawable.text_border_selector)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textViewRegGivenNameMessage.text = ""
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0) {
                    textViewRegEmailLabel.text = ""
                }
            }

        })


        signUp.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        val username = editTextRegUserId.text.toString()
        if (username.isNullOrBlank()) {
            editTextRegUserId.setBackgroundResource(R.drawable.text_border_error)
            return
        }

        val pwd = editTextRegUserPassword.text.toString()
        if (pwd.isNullOrBlank()) {
            editTextRegUserPassword.setBackgroundResource(R.drawable.text_border_error)
            return
        }

        val email = editTextRegEmail.text.toString()
        if (email.isNullOrBlank()) {
            editTextRegEmail.setBackgroundResource(R.drawable.text_border_error)
            return
        }

        showWaitDialog("Signing up...")
        Amplify.Auth.signUp(
            username,
            pwd,
            AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), "eugene.chen3.14@gmail.com").build(),
            { result ->
                closeWaitDialog()
                if (result.isSignUpComplete) {
                    showDialogMessage("Sign up successful!", "$username has been Confirmed") {
                        exit()
                    }
                } else {
                    start<SignUpConfirmActivity>(Pair<String, String>("username", username))
                }
            },
            { error ->
                closeWaitDialog()
                showDialogMessage("Sign up failed", error.message ?: "", null)
            }
        )


    }

    private fun exit() {
        val intent = Intent()
        intent.putExtra("username", username)
        intent.putExtra("password", pwd)
        setResult(RESULT_OK, intent)
        finish()
    }

}