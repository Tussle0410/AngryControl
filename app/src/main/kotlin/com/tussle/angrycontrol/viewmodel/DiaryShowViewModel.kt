package com.tussle.angrycontrol.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tussle.angrycontrol.Event.Event
import com.tussle.angrycontrol.model.DB.Repo
import com.tussle.angrycontrol.model.DateAndDiary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiaryShowViewModel(private val repo : Repo) : ViewModel(){
    private val _deleteEvent = MutableLiveData<Event<Boolean>>()
    val deleteEvent : LiveData<Event<Boolean>>
        get() = _deleteEvent
    val diaryInfo = MutableLiveData<DateAndDiary>()
    fun setDiaryInfo(info : DateAndDiary){
        diaryInfo.value = info
    }
    fun deleteDiary(){
        CoroutineScope(Dispatchers.IO).launch {
            repo.deleteAngryDiary(diaryInfo.value!!.angryDiary.diary_id)
            _deleteEvent.postValue(Event(true))
        }
    }
}