package com.eugene.mydemo.utils

import android.app.Activity
import android.content.Intent

inline fun <reified T> Activity.start(vararg args: Pair<String, String>) {
    val i = Intent(this, T::class.java)
    args.forEach {
        i.putExtra(it.first, it.second)
    }
    startActivity(i)
}


inline fun <reified T> Activity.startForResult(
    requestCode: Int,
    vararg args: Pair<String, String>
) {
    val i = Intent(this, T::class.java)
    args.forEach {
        i.putExtra(it.first, it.second)
    }
    startActivityForResult(i, requestCode)
}
