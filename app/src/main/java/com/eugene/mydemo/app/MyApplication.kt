package com.eugene.mydemo.app

import android.app.Application
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify

class MyApplication : Application(){

    companion object {
        var instance: MyApplication? = null
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        AWSMobileClient.getInstance().initialize(applicationContext, object :
            Callback<UserStateDetails> {
            override fun onError(e: Exception?) {
                Log.e("MyAmplifyApp", "An error occurred while tried to init the AWSMobileClient")
            }
            override fun onResult(result: UserStateDetails?) {
                Log.d("MyAmplifyApp","Successfully started the AWSMobileClient: ${result?.userState}") // Reaches here with SIGNED_OUT
            }
        })


        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Log.i("MyAmplifyApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }
    }


}