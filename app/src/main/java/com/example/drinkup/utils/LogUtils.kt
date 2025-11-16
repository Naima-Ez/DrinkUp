package com.example.drinkup.utils

import android.util.Log
import com.github.mikephil.charting.BuildConfig

object LogUtils {

    private const val TAG = "DrinkUp"

    fun d(message: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun e(message: String, throwable: Throwable? = null, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }

    fun i(message: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    fun w(message: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message)
        }
    }

    fun v(message: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, message)
        }
    }
}
