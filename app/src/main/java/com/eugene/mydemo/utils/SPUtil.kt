package com.eugene.mydemo.utils

import android.content.Context
import android.content.SharedPreferences
import com.eugene.mydemo.app.MyApplication
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

object SPUtil {

    const val FILE_NAME = "default"

    inline fun <reified V> put(key: String, value: V) {

        val sp = MyApplication.instance?.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sp?.edit()

        when (value) {
            is String -> {
                editor?.putString(key, value)
            }
            is Int -> {
                editor?.putInt(key, value)
            }
            is Boolean -> {
                editor?.putBoolean(key, value)
            }
            is Float -> {
                editor?.putFloat(key, value)
            }
            is Long -> {
                editor?.putLong(key, value)
            }
        }
        SharedPreferencesCompat.apply(editor!!)
    }

    inline fun <reified V> get(key: String, defValue: V): V {
        val sp = MyApplication.instance?.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE)

        return when (defValue) {
            is String -> {
                sp?.getString(key, defValue) as V
            }
            is Int -> {
                sp?.getInt(key, defValue) as V
            }
            is Boolean -> {
                sp?.getBoolean(key, defValue) as V
            }
            is Float -> {
                sp?.getFloat(key, defValue) as V
            }
            is Long -> {
                sp?.getLong(key, defValue) as V
            }
            else -> defValue
        }
    }

    fun remove(key: String) {
        val sp = MyApplication.instance?.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE)
        val editor = sp?.edit()
        editor?.remove(key)
        SharedPreferencesCompat.apply(editor!!)
    }

    fun clear() {
        val sp = MyApplication.instance?.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE)
        val editor = sp?.edit()
        editor?.clear()
        SharedPreferencesCompat.apply(editor!!)
    }

    object SharedPreferencesCompat {
        private val sApplyMethod = findApplyMethod()

        private fun findApplyMethod(): Method? {
            try {
                val clz = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }
            return null
        }

        fun apply(editor: SharedPreferences.Editor) {
            try {
                sApplyMethod?.invoke(editor) ?: return
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            }

            editor.commit()
        }
    }

}