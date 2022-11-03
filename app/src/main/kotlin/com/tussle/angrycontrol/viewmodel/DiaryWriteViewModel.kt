package com.tussle.angrycontrol.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tussle.angrycontrol.sharedPreference.GlobalApplication
import java.text.SimpleDateFormat
import java.util.*

class DiaryWriteViewModel : ViewModel() {
    var writeKinds : Int = 0
    val diaryText = MutableLiveData<String>()
    val saveCheck = MutableLiveData<Boolean>()
    var angryDegree : Int = 0
    var writeDate : String = "2222"
    fun setWriteDate(){
        val date = Calendar.getInstance().time
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        writeDate = format.format(date).toString()
    }
    fun diaryBackUp(){
        with(GlobalApplication){
            pref.writeSetBoolean("saveCheck", true)
            pref.writeSetString("saveDiary", diaryText.value!!)
            pref.writeSetInt("saveDegree", angryDegree)
        }
    }
    fun diaryRestore(){
        with(GlobalApplication){
            diaryText.value = pref.writeGetString("saveDiary","")
            angryDegree = pref.writeGetInt("saveDegree", 0)
        }
    }
    fun setSaveCheck(){
        saveCheck.value = GlobalApplication.pref.writeGetBoolean("saveCheck", false)
    }
    fun setKind(kind : Int){
        writeKinds = kind
    }
    fun setDegree(degree : Int){
        angryDegree = degree
    }
}