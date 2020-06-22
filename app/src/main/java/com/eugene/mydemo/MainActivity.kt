package com.eugene.mydemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException
import com.amplifyframework.core.Amplify
import com.eugene.mydemo.app.BaseActivity
import com.eugene.mydemo.utils.SPUtil
import com.eugene.mydemo.utils.start
import com.eugene.mydemo.utils.startForResult
import kotlinx.android.synthetic.main.activity_main.*

const val HAS_SIGN_IN = "HAS_SIGN_IN"

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val signIn = SPUtil.get<Boolean>(HAS_SIGN_IN, false)

        if (signIn) {
            start<HomeActivity>()
            finish()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Amplify.Auth.fetchAuthSession(
            { result -> Log.i("AmplifyQuickstart", result.toString()) },
            { error -> Log.e("AmplifyQuickstart", error.toString()) }
        )

        initView()
    }


    private fun initView() {

        editTextUserId.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 0) {
                    textViewUserIdLabel.setText(R.string.Username)
                    editTextUserId.setBackgroundResource(R.drawable.text_border_selector)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textViewUserIdMessage.text = ""
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0) {
                    textViewUserIdLabel.text = ""
                }
            }

        })

        editTextUserPassword.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 0) {
                    textViewUserPasswordLabel.setText(R.string.Username)
                    editTextUserPassword.setBackgroundResource(R.drawable.text_border_selector)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textViewUserPasswordMessage.text = ""
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0) {
                    textViewUserPasswordLabel.text = ""
                }
            }

        })

        buttonLogIn.setOnClickListener {

            signIn()

        }

        textViewUserSignUp.setOnClickListener {
            startForResult<RegisterActivity>(1)
        }

        tvFacebook.setOnClickListener {
            start<FacebookActivity>()
//            Amplify.Auth.signInWithSocialWebUI(
//                AuthProvider.facebook(),
//                this,
//                { result ->
//                    start<HomeActivity>()
//                    finish()
//                },
//                { error -> Log.e("AuthQuickstart", error.toString()) }
//            )
        }
    }

    private fun signIn() {
        val username = editTextUserId.text.toString()
        if (username.isNullOrBlank()) {
            editTextUserId.setBackgroundResource(R.drawable.text_border_error)
            return
        }

        val pwd = editTextUserPassword.text.toString()
        if (pwd.isNullOrBlank()) {
            editTextUserPassword.setBackgroundResource(R.drawable.text_border_error)
            return
        }

        showWaitDialog("Signing in...")

        Amplify.Auth.signIn(
            username,
            pwd,
            { result ->
                if (result.isSignInComplete) {
                    SPUtil.put(HAS_SIGN_IN, true)
                    closeWaitDialog()
                    start<HomeActivity>()
                    finish()
                } else {
                    startForResult<SignUpConfirmActivity>(2)
                }

            },
            { error ->
                closeWaitDialog()

                val e = error.cause

                if (e is UserNotConfirmedException) {
                    showDialogMessage("Sign-in failed",e.errorMessage) {
                        startForResult<SignUpConfirmActivity>(2, Pair("username", username))
                    }
                }

                textViewUserIdMessage.text = "Sign-in failed"
                editTextUserPassword.background = getDrawable(R.drawable.text_border_error)

                textViewUserIdMessage.text = "Sign-in failed"
                editTextUserId.background = getDrawable(R.drawable.text_border_error)

            }
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Register user
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val username = data?.getStringExtra("username")
                    if (!username.isNullOrBlank()) {
                        editTextUserId.setText(username)
                    }
                    val pwd = data?.getStringExtra("password")
                    if (!pwd.isNullOrBlank()) {
                        editTextUserPassword.setText(pwd)
                    }
                }
            }
            // Confirm register user
            2 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val username = data?.getStringExtra("username")
                    if (!username.isNullOrBlank()) {
                        editTextUserId.setText(username)
                    }
                }
            }

        }
    }

}