package com.tussle.angrycontrol.sharedPreference

import android.content.Context

class PreferencesUtil(context : Context){
    private val settingPrefs = context.getSharedPreferences("Setting", 0)
    private val writePrefs = context.getSharedPreferences("Write", 0)
    fun settingSetString(key : String, str : String){
        settingPrefs.edit().putString(key, str).apply()
    }
    fun settingSetInt(key : String, value : Int){
        settingPrefs.edit().putInt(key, value).apply()
    }
    fun settingGetString(key : String, str : String) : String
        =settingPrefs.getString(key, str).toString()
    fun settingGetInt(key : String, default : Int) : Int
        =settingPrefs.getInt(key, default)
    fun writeGetBoolean(key : String, default : Boolean)
        =writePrefs.getBoolean(key, default)
    fun writeSetBoolean(key : String, value : Boolean){
        writePrefs.edit().putBoolean(key, value).apply()
    }
    fun writeGetString(key : String, default : String)
        =writePrefs.getString(key, default)
    fun writeSetString(key : String, value : String){
        writePrefs.edit().putString(key, value).apply()
    }
    fun writeGetInt(key : String, default : Int)
        =writePrefs.getInt(key, default)
    fun writeSetInt(key : String, value : Int){
        writePrefs.edit().putInt(key, value).apply()
    }
    fun writeGetLong(key : String, default : Long)
            =writePrefs.getLong(key, default)
    fun writeSetLong(key : String, value : Long){
        writePrefs.edit().putLong(key, value).apply()
    }
}