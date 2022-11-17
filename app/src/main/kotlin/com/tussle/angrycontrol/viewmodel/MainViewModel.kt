package com.tussle.angrycontrol.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.navigation.NavigationBarView
import com.tussle.angrycontrol.Event.Event
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.model.AngryDate
import com.tussle.angrycontrol.model.AngryDiary
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
    private val _angryCountEvent = MutableLiveData<Event<Boolean>>()
    private var consumeTime = 0
    private var idCount =  MutableLiveData<Int>()
    private var diaryConditionStart = LocalDateTime.MIN
    private var diaryConditionEnd = LocalDateTime.MAX
    private var chartConditionStart = LocalDateTime.MIN
    private var chartConditionEnd = LocalDateTime.MAX
    val angryDateAndDiary = MutableLiveData<MutableList<DateAndDiary>>()
    val angryDiary = mutableListOf<DateAndDiary>()
    val calendarDiary = mutableListOf<DateAndDiary>()
    var degreeCount = mutableListOf<Int>()
    val angryCountEvent : LiveData<Event<Boolean>>
        get() = _angryCountEvent
    var countAngryDegree : Int = 0
    var countDownStartTime : String = "10"
    val mainFragment : LiveData<MainNaviMenu>
        get() =  _mainFragment
    val chartDegree1 = MutableLiveData<String>()
    val chartDegree2 = MutableLiveData<String>()
    val chartDegree3 = MutableLiveData<String>()
    val chartDegree4 = MutableLiveData<String>()
    val chartDegree5 = MutableLiveData<String>()
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
        idCount.value = idCount.value!!.plus(1)
    }
    fun getIdCount()
        = idCount.value
    fun insertAngryDate(){
        val time = LocalDateTime.now()
        CoroutineScope(Dispatchers.IO).launch {
            repo.insertAngryDate(countAngryDegree, time.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli())
            repo.insertAngryCount(idCount.value!!, consumeTime)
            _angryCountEvent.postValue(Event(true))
        }
        angryDateAndDiary.value!!.add(DateAndDiary(AngryDate(idCount.value!!, countAngryDegree, time),AngryDiary(null, null, null)))
    }
    fun selectAngryDateAndDiary(){
        CoroutineScope(Dispatchers.IO).launch {
            repo.selectAngryDateAndDiary().let {
                angryDateAndDiary.postValue(it)
            }
            repo.selectAngryCount().let {
                idCount.postValue(it+1)
            }
        }
    }
    fun setDegreeCount(){
        degreeCount = mutableListOf(0, 0, 0, 0, 0)
        for(info in angryDateAndDiary.value!!){
            degreeCount[info.angryDate.angryDegree - 1] += 1
        }
    }
    fun setDiaryList(){
        angryDiary.clear()
        for(cur in angryDateAndDiary.value!!){
            if(cur.angryDiary == null)
                continue
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
    fun setChartCondition(check : Boolean, conditionStart : LocalDateTime?, conditionEnd: LocalDateTime?){
        if(check){
            chartConditionStart = LocalDateTime.MIN
            chartConditionEnd = LocalDateTime.MAX
        }else{
            chartConditionStart = conditionStart
            chartConditionEnd = conditionEnd
        }
    }
    fun setCalendarDiary(conditionStart : LocalDateTime, conditionEnd: LocalDateTime) : MutableList<DateAndDiary>{
        val chartList = mutableListOf<DateAndDiary>()
        calendarDiary.clear()
        for(info in angryDateAndDiary.value!!){
            if(info.angryDate.date.isAfter(conditionStart) && info.angryDate.date.isBefore(conditionEnd)){
                chartList.add(info)
                if(info.angryDiary != null)
                    calendarDiary.add(info)
            }
        }
        return chartList
    }
    fun setChartDegree(){
        chartDegree1.value = degreeCount[0].toString()
        chartDegree2.value = degreeCount[1].toString()
        chartDegree3.value = degreeCount[2].toString()
        chartDegree4.value = degreeCount[3].toString()
        chartDegree5.value = degreeCount[4].toString()
    }
    fun stringToMillisSecond()
        = countDownStartTime.toLong() * 1000

    fun millisSecondToSecond(time : Long)
        = (time/1000).toString()
}