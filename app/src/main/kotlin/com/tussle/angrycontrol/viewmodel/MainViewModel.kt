package com.tussle.angrycontrol.viewmodel


import android.util.Log
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
    private val _angryCountEvent = MutableLiveData<Event<Boolean>>()
    private val _diarySelectEvent = MutableLiveData<Event<Boolean>>()
    private var consumeTime = 0
    private var idCount =  MutableLiveData<Int>()
    private var diaryConditionStart = LocalDateTime.MIN
    private var diaryConditionEnd = LocalDateTime.MAX
    private var chartConditionStart = LocalDateTime.MIN
    private var chartConditionEnd = LocalDateTime.MAX
    var calendarCurDate : LocalDateTime
    val angryDateAndDiary = MutableLiveData<MutableList<DateAndDiary>>()
    val angryDiary = mutableListOf<DateAndDiary>()
    var degreeCount = mutableListOf<Int>()
    val angryCountEvent : LiveData<Event<Boolean>>
        get() = _angryCountEvent
    val diarySelectEvent : LiveData<Event<Boolean>>
        get() = _diarySelectEvent
    var countAngryDegree : Int = 0
    lateinit var countDownStartTime : String
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
    init {
        val tempDate = LocalDateTime.now()
        calendarCurDate = getStartTime(tempDate.year, tempDate.monthValue, tempDate.dayOfMonth)
        setCountStartTime()
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
            repo.insertAngryDate(idCount.value!!,countAngryDegree, time.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli())
            repo.insertAngryCount(idCount.value!!, consumeTime)
            _angryCountEvent.postValue(Event(true))
        }
        angryDateAndDiary.value!!.add(DateAndDiary(AngryDate(idCount.value!!, countAngryDegree, time), null))
    }
    fun selectAngryDateAndDiary(){
        CoroutineScope(Dispatchers.IO).launch {
            repo.selectAngryDateAndDiary().let {
                angryDateAndDiary.postValue(it)
            }
            _diarySelectEvent.postValue(Event(true))
            repo.selectAngryCount().let {
                if(it == null){
                    idCount.postValue(1)
                }else
                    idCount.postValue(it+1)
            }
        }
    }
    fun setDegreeCount(){
        degreeCount = mutableListOf(0, 0, 0, 0, 0)
        Log.d("정보", "$chartConditionStart , $chartConditionEnd")
        for(info in angryDateAndDiary.value!!){
            with(info.angryDate){
                if(this.date.isAfter(chartConditionStart) && this.date.isBefore(chartConditionEnd))
                    degreeCount[this.angryDegree - 1] += 1
            }
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
    fun getCalendarList(conditionStart : LocalDateTime, conditionEnd: LocalDateTime) : MutableList<DateAndDiary>{
        val calendarList = mutableListOf<DateAndDiary>()
        for(info in angryDateAndDiary.value!!){
            if(info.angryDate.date.isAfter(conditionStart) && info.angryDate.date.isBefore(conditionEnd))
                calendarList.add(info)
        }
        return calendarList
    }
    fun getCalendarDiary(list : MutableList<DateAndDiary>) : MutableList<DateAndDiary>{
        val calendarDiary = mutableListOf<DateAndDiary>()
        for(info in list){
            if(info.angryDiary!=null){
                calendarDiary.add(info)
            }
        }
        return calendarDiary
    }
    fun setChartDegree(){
        chartDegree1.value = degreeCount[0].toString()
        chartDegree2.value = degreeCount[1].toString()
        chartDegree3.value = degreeCount[2].toString()
        chartDegree4.value = degreeCount[3].toString()
        chartDegree5.value = degreeCount[4].toString()
    }
    fun setCalendarDate(date: LocalDateTime){
        calendarCurDate = getStartTime(date.year, date.monthValue, date.dayOfMonth)
    }
    fun getStartTime(year : Int, month : Int, day : Int) : LocalDateTime
            = LocalDateTime.of(year, month, day, 0, 0, 0)
    fun getEndTime(): LocalDateTime
    = calendarCurDate.plusDays(1)
    fun getAngryCount() : Int?
        = angryDateAndDiary.value?.size
    fun stringToMillisSecond()
        = countDownStartTime.toLong() * 1000

    fun millisSecondToSecond(time : Long)
        = (time/1000).toString()
}