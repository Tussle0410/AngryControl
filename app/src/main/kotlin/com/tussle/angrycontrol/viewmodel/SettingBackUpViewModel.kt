package com.tussle.angrycontrol.viewmodel

import androidx.lifecycle.ViewModel
import com.tussle.angrycontrol.sharedPreference.GlobalApplication
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId

class SettingBackUpViewModel : ViewModel() {
    var backUpDate = GlobalApplication.pref.settingGetString("backUpDate","")
    private val formatter = SimpleDateFormat("yyyy-MM-dd")
    fun setBackUpDate(){
        val date = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        backUpDate = formatter.format(date).toString()
        GlobalApplication.pref.settingSetString("backUpDate", backUpDate)
    }
}