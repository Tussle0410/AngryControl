package com.tussle.angrycontrol.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.navigation.NavigationBarView
import com.tussle.angrycontrol.Event.Event
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.model.AngryDate
import com.tussle.angrycontrol.model.DB.Repo
import com.tussle.angrycontrol.model.DateAndDiary
import com.tussle.angrycontrol.model.MainNaviMenu
import com.tussle.angrycontrol.sharedPreference.GlobalApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

class MainViewModel(private val repo : Repo) : ViewModel() {
    private val _mainFragment = MutableLiveData(MainNaviMenu.Count)
    private var idCount =  GlobalApplication.pref.writeGetInt("id", 1)
    private val _angryCountEvent = MutableLiveData<Event<Boolean>>()
    private var consumeTime = 0
    private val angryDate = MutableLiveData<MutableList<AngryDate>>()
    private var diaryConditionStart = LocalDateTime.MIN
    private var diaryConditionEnd = LocalDateTime.MAX
    val angryDateAndDiary = MutableLiveData<MutableList<DateAndDiary>>()
    val angryDiary = mutableListOf<DateAndDiary>()
    var degreeCount = mutableListOf<Int>()
    val angryCountEvent : LiveData<Event<Boolean>>
        get() = _angryCountEvent
    var countAngryDegree : Int = 0
    var countDownStartTime : String = "10"
    val mainFragment : LiveData<MainNaviMenu>
        get() =  _mainFragment
    val bottomMenuClickListener =
        NavigationBarView.OnItemSelectedListener { item ->
            val fragment = getFragment(item.itemId)
            fragmentChange(fragment)
            true
    }
    //BottomNavi Fragment Change Method
    private fun getFragment(menu_id : Int) : MainNaviMenu
        = when(menu_id){
            R.id.count_menu -> MainNaviMenu.Count
            R.id.diary_menu -> MainNaviMenu.Diary
            R.id.calendar_menu -> MainNaviMenu.Calendar
            R.id.chart_menu -> MainNaviMenu.Chart
            R.id.setting_menu -> MainNaviMenu.Setting
            else -> throw IllegalArgumentException("Not Found Menu_id")
        }
    private fun fragmentChange(fragment : MainNaviMenu){
        if(_mainFragment.value != fragment)
            _mainFragment.value = fragment
    }
    fun setIdCount(){
        idCount = GlobalApplication.pref.writeGetInt("id", 1)
    }
    fun initCountAngryDegree(){
        countAngryDegree = 0
    }
    fun changeCountAngryDegree(degree : Int){
        countAngryDegree = degree
    }
    fun setCountStartTime(){
        countDownStartTime = GlobalApplication.pref.settingGetString("countStart", "10")
    }
    fun initConsumeTime(){
        consumeTime = 0
    }
    fun plusConsumeTime(){
        consumeTime += countDownStartTime.toInt()
    }
    fun plusId(){
        GlobalApplication.pref.writeSetInt("id", ++idCount)
    }
    fun insertAngryDate(){
        val time = LocalDateTime.now()
        CoroutineScope(Dispatchers.IO).launch {
            repo.insertAngryDate(countAngryDegree, time.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli())
            repo.insertAngryCount(idCount, consumeTime)
            _angryCountEvent.postValue(Event(true))
        }
        angryDate.value!!.add(AngryDate(idCount, countAngryDegree, time))
    }
    fun selectAngryDate(){
        CoroutineScope(Dispatchers.IO).launch {
            repo.selectAngryDate().let {
                angryDate.postValue(it)
            }
        }
    }
    fun selectAngryDateAndDiary(){
        CoroutineScope(Dispatchers.IO).launch {
            repo.selectAngryDateAndDiary().let {
                angryDateAndDiary.postValue(it)
            }
        }
    }
    fun setDegreeCount(){
        degreeCount = mutableListOf(0, 0, 0, 0, 0)
        for(info in angryDate.value!!){
            if(info.angryDegree!=0)
                degreeCount[info.angryDegree - 1] += 1
        }
    }
    fun setDiaryList(){
        angryDiary.clear()
        for(cur in angryDateAndDiary.value!!){
            if(cur.angryDate.date.isAfter(diaryConditionStart) &&
                    cur.angryDate.date.isBefore(diaryConditionEnd))
                angryDiary.add(cur)
        }
    }
    fun setDiaryCondition(check : Boolean, conditionStart : LocalDateTime?, conditionEnd: LocalDateTime?){
        if(check){
            diaryConditionStart = LocalDateTime.MIN
            diaryConditionEnd = LocalDateTime.MAX
        }else{
            diaryConditionStart = conditionStart
            diaryConditionEnd = conditionEnd
        }
    }
    fun setChartDiary(conditionStart : LocalDateTime, conditionEnd: LocalDateTime) : MutableList<DateAndDiary>{
        val chartList = mutableListOf<DateAndDiary>()
        for(info in angryDateAndDiary.value!!){
            if(info.angryDate.date.isAfter(conditionStart) && info.angryDate.date.isBefore(conditionEnd))
                chartList.add(info)
        }
        return chartList
    }
    fun stringToMillisSecond()
        = countDownStartTime.toLong() * 1000

    fun millisSecondToSecond(time : Long)
        = (time/1000).toString()
}