package com.tussle.angrycontrol.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tussle.angrycontrol.sharedPreference.GlobalApplication
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId

class SettingBackUpViewModel : ViewModel() {
    val backUpDate = MutableLiveData<String>()
    private val formatter = SimpleDateFormat("yyyy-MM-dd")
    init {
        backUpDate.value = GlobalApplication.pref.settingGetString("backUpDate","")
    }
    fun setBackUpDate(){
        val date = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        backUpDate.value = formatter.format(date).toString()
        GlobalApplication.pref.settingSetString("backUpDate", backUpDate.value!!)
    }
}