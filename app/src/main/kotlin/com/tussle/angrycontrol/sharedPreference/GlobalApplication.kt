package com.tussle.angrycontrol.sharedPreference

import android.app.Application
import androidx.room.Room
import com.tussle.angrycontrol.model.DB.DB

class GlobalApplication : Application() {
    companion object{
        lateinit var pref : PreferencesUtil

        lateinit var appInstance : GlobalApplication
            private set
        lateinit var DBInstance : DB
            private set
    }
    override fun onCreate() {
        super.onCreate()
        pref = PreferencesUtil(applicationContext)
        appInstance = this
        DBInstance = Room.databaseBuilder(
            appInstance,
            DB::class.java,
            "DB"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}