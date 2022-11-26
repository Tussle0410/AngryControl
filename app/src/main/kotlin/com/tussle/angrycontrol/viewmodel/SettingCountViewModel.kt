package com.tussle.angrycontrol.viewmodel

import androidx.lifecycle.ViewModel
import com.tussle.angrycontrol.sharedPreference.GlobalApplication

class SettingCountViewModel : ViewModel() {
    val countStart = GlobalApplication.pref.settingGetString("countStart", "10")
    val countEffect = GlobalApplication.pref.settingGetString("countEffect", "Vibration")
}