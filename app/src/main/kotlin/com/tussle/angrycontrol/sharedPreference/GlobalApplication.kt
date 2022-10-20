package com.tussle.angrycontrol.sharedPreference

import android.app.Application

class GlobalApplication : Application() {
    companion object{
        lateinit var pref : PreferencesUtil

    }
    override fun onCreate() {
        super.onCreate()
        pref = PreferencesUtil(applicationContext)
    }
}