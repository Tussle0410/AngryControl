package com.tussle.angrycontrol.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tussle.angrycontrol.Event.Event
import com.tussle.angrycontrol.model.DB.Repo
import com.tussle.angrycontrol.model.DateAndDiary
import com.tussle.angrycontrol.sharedPreference.GlobalApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId

class DiaryWriteViewModel(private val repo : Repo) : ViewModel() {
    private var writeDateLong = 0L
    var diaryId = 0
    private var countDiaryCheck = false
    private val _insertEvent =  MutableLiveData<Event<Boolean>>()
    private val _updateEvent = MutableLiveData<Event<Boolean>>()
    private var curSave = false
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val insertEvent : LiveData<Event<Boolean>>
        get() = _insertEvent
    val updateEvent : LiveData<Event<Boolean>>
        get() = _updateEvent
    var writeKinds : Int = 0
    val diaryText = MutableLiveData<String>()
    val saveCheck = MutableLiveData<Boolean>()
    var angryDegree : Int = 0
    val writeDate = MutableLiveData<String>()
    fun setWriteDate(check : Boolean){
        writeDateLong =
            if(check)
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            else
                GlobalApplication.pref.writeGetLong("saveDate", 0L)
        writeDate.value = dateFormat.format(writeDateLong).toString()
    }
    fun diaryBackUp(){
        with(GlobalApplication){
            pref.writeSetBoolean("saveCheck", true)
            pref.writeSetString("saveDiary", diaryText.value!!)
            pref.writeSetInt("saveDegree", angryDegree)
            pref.writeSetLong("saveDate", writeDateLong)
            pref.writeSetBoolean("countDiaryCheck", countDiaryCheck)
            if(countDiaryCheck)
                pref.writeSetInt("saveId", diaryId)
        }
    }
    fun diaryRestore(){
        with(GlobalApplication){
            diaryText.value = pref.writeGetString("saveDiary","")
            angryDegree = pref.writeGetInt("saveDegree", 0)
            getCountDiaryCheck(true)
            writeDateLong = pref.writeGetLong("saveDate", 0)
            if(countDiaryCheck)
                diaryId = pref.writeGetInt("saveId", 1)
        }
    }
    fun insertDiary(){
        CoroutineScope(Dispatchers.IO).launch {
            if(!countDiaryCheck)
                repo.insertAngryDate(diaryId,angryDegree, writeDateLong)
            else
                repo.updateAngryDate(angryDegree, diaryId)
            repo.insertAngryDiary(diaryId, diaryText.value!!)
            _insertEvent.postValue(Event(true))
        }
    }
    fun updateDiary(){
        CoroutineScope(Dispatchers.IO).launch {
            repo.updateAngryDiary(diaryText.value!!, diaryId)
            repo.updateAngryDate(angryDegree, diaryId)
            _updateEvent.postValue(Event(true))
        }
    }
    @JvmName("setDiaryId1")
    fun setDiaryId(value : Int){
        diaryId = value
    }
    fun getSaveCheck(){
        saveCheck.value = GlobalApplication.pref.writeGetBoolean("saveCheck", false)
    }
    fun setSaveCheck(){
        if(curSave)
            GlobalApplication.pref.writeSetBoolean("saveCheck", false)
    }
    fun getCountDiaryCheck(check : Boolean){
        countDiaryCheck = if(!check)
            true
        else
            GlobalApplication.pref.writeGetBoolean("countDiaryCheck", false)
    }
    fun setCountDiaryCheck(){
        if(curSave)
            GlobalApplication.pref.writeSetBoolean("countDiaryCheck", false)
    }
    fun setCurSave(){
        curSave = true
    }
    fun setKind(kind : Int){
        writeKinds = kind
    }
    fun setDegree(degree : Int){
        angryDegree = degree
    }
    fun setDiaryInfo(info : DateAndDiary){
        diaryText.value = info.angryDiary!!.content
        angryDegree = info.angryDate.angryDegree
        diaryId = info.angryDiary.diary_id!!
        writeDateLong = info.angryDate.date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        writeDate.value = dateFormat.format(writeDateLong)
    }
}