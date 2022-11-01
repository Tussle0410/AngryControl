package com.tussle.angrycontrol.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.navigation.NavigationBarView
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.model.MainNaviMenu
import com.tussle.angrycontrol.sharedPreference.GlobalApplication

class MainViewModel : ViewModel() {
    private val _mainFragment = MutableLiveData(MainNaviMenu.Count)
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
    fun initCountAngryDegree(){
        countAngryDegree = 0
    }
    fun changeCountAngryDegree(degree : Int){
        countAngryDegree = degree
    }
    fun setCountStartTime(){
        countDownStartTime = GlobalApplication.pref.settingGetString("countStart", "10")
    }
    fun stringToMillisSecond()
        = countDownStartTime.toLong() * 1000
    fun millisSecondToSecond(time : Long)
        = (time/1000).toString()
}