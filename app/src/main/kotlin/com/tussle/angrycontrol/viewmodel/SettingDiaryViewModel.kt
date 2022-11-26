package com.tussle.angrycontrol.viewmodel

import androidx.lifecycle.ViewModel
import com.tussle.angrycontrol.sharedPreference.GlobalApplication

class SettingDiaryViewModel : ViewModel() {
    val tempSave = GlobalApplication.pref.settingGetInt("tempSave", 1)
}