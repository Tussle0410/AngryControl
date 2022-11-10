package com.tussle.angrycontrol.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tussle.angrycontrol.model.DB.Repo
import com.tussle.angrycontrol.model.DateAndDiary

class DiaryShowViewModel(repo : Repo) : ViewModel(){
    val DiaryInfo = MutableLiveData<DateAndDiary>()
    fun setDiaryInfo(info : DateAndDiary){
        DiaryInfo.value = info
    }
}