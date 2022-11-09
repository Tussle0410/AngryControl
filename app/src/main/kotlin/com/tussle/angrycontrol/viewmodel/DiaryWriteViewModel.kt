package com.tussle.angrycontrol.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tussle.angrycontrol.Event.Event
import com.tussle.angrycontrol.model.DB.Repo
import com.tussle.angrycontrol.sharedPreference.GlobalApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

class DiaryWriteViewModel(private val repo : Repo) : ViewModel() {
    private var writeDateLong = 0L
    private var diaryId = GlobalApplication.pref.writeGetInt("id", 1)
    private var countDiaryCheck = false
    private val _insertEvent =  MutableLiveData<Event<Boolean>>()
    private var curSave = false
    val insertEvent : LiveData<Event<Boolean>>
        get() = _insertEvent
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
        val format = SimpleDateFormat("yyyy-MM-dd")
        writeDate.value = format.format(writeDateLong).toString()
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
                repo.insertAngryDate(angryDegree, writeDateLong)
            repo.insertAngryDiary(diaryId, diaryText.value!!)
            _insertEvent.postValue(Event(true))
        }
    }
    fun plusId(){
        if(!countDiaryCheck)
            GlobalApplication.pref.writeSetInt("id", ++diaryId)
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
}