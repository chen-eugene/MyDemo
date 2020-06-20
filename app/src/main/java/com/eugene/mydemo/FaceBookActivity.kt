package com.eugene.mydemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.core.Amplify
import com.eugene.mydemo.app.BaseActivity

class FaceBookActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Amplify.Auth.signInWithSocialWebUI(
            AuthProvider.facebook(),
            this,
            { result -> Log.i("AuthQuickstart", result.toString()) },
            { error -> Log.e("AuthQuickstart", error.toString()) }
        )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(intent?.scheme != null && "myapp" == intent.scheme) {
            Amplify.Auth.handleWebUISignInResponse(intent)
        }
    }


}