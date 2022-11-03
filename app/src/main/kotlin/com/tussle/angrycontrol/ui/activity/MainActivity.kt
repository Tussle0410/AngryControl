package com.tussle.angrycontrol.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.ActivityMainBinding
import com.tussle.angrycontrol.model.DB.Repo
import com.tussle.angrycontrol.model.DB.RepoFactory
import com.tussle.angrycontrol.model.MainNaviMenu
import com.tussle.angrycontrol.ui.fragment.*
import com.tussle.angrycontrol.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel : MainViewModel by lazy {
        val factory = RepoFactory(Repo())
        ViewModelProvider(this, factory)[MainViewModel::class.java]
    }
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        init()
    }
    private fun init(){
        fragmentObserver()
    }
    private fun fragmentObserver(){
        viewModel.mainFragment.observe(this, Observer {
            changeFragment(it)
        })
    }
    private fun changeFragment(fragment_tag : MainNaviMenu){
        val transaction = supportFragmentManager.beginTransaction()
        var targetFragment = supportFragmentManager.findFragmentByTag(fragment_tag.tag)
        if(targetFragment == null){
            targetFragment = when(fragment_tag){
                MainNaviMenu.Count -> CountFragment.getInstance()
                MainNaviMenu.Diary -> DiaryFragment.getInstance()
                MainNaviMenu.Calendar -> CalendarFragment.getInstance()
                MainNaviMenu.Chart -> ChartFragment.getInstance()
                MainNaviMenu.Setting -> SettingFragment.getInstance()
            }
            transaction.add(R.id.main_frame, targetFragment, fragment_tag.tag)
        }
        transaction.show(targetFragment)
        MainNaviMenu.values()
            .filterNot { it == fragment_tag }
            .forEach { menu ->
                supportFragmentManager.findFragmentByTag(menu.tag)?.let {
                    transaction.hide(it)
                }
            }
        transaction.commitNowAllowingStateLoss()
    }
}