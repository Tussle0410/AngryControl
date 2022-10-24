package com.tussle.angrycontrol.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.navigation.NavigationBarView
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.model.MainNaviMenu

class MainViewModel : ViewModel() {
    private val _mainFragment = MutableLiveData(MainNaviMenu.Count)
    private val _countDownStartTime = MutableLiveData<String>()
    val mainFragment : LiveData<MainNaviMenu>
        get() =  _mainFragment
    val countDownStartTime : LiveData<String>
        get() = _countDownStartTime
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
    fun setCountStartTime(time : String){
        _countDownStartTime.value = time
    }
    fun stringToMillisSecond(time : String)
        = time.toLong() * 1000
    fun millisSecondToSecond(time : Long)
        = (time/1000).toString()
}