package com.tussle.angrycontrol.sharedPreference

import android.content.Context

class PreferencesUtil(context : Context){
    private val settingPrefs = context.getSharedPreferences("Setting", 0)
    fun settingSetString(key : String, str : String){
        settingPrefs.edit().putString(key, str).apply()
    }
    fun settingGetString(key : String, str : String) : String
        =settingPrefs.getString(key, str).toString()
}