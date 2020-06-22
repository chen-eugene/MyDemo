package com.eugene.mydemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.core.Amplify
import com.eugene.mydemo.app.BaseActivity
import com.eugene.mydemo.utils.start

class FacebookActivity : BaseActivity(){


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(intent?.scheme != null && "maji".equals(intent?.scheme)) {
            Amplify.Auth.handleWebUISignInResponse(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Amplify.Auth.signInWithSocialWebUI(
            AuthProvider.facebook(),
            this,
            { result ->
                start<HomeActivity>()
                finish()
            },
            { error -> Log.e("AuthQuickstart", error.toString()) }
        )
    }


}