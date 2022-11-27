package com.tussle.angrycontrol.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.SettingDiaryLayoutBinding
import com.tussle.angrycontrol.sharedPreference.GlobalApplication
import com.tussle.angrycontrol.viewmodel.SettingDiaryViewModel

class SettingDiaryActivity : AppCompatActivity() {
    private val viewModel : SettingDiaryViewModel by lazy {
        ViewModelProvider(this).get(SettingDiaryViewModel::class.java)
    }
    private lateinit var binding : SettingDiaryLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.setting_diary_layout)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        init()
    }
    private fun init(){
        setButton()
        setSwitch()
    }
    private fun setButton(){
        binding.settingDiaryBackButton.setOnClickListener {
            finish()
        }
    }
    private fun setSwitch(){
        if(viewModel.tempSave == 1)
            binding.settingDiaryTempSaveSwitch.isChecked = true
        binding.settingDiaryTempSaveSwitch.setOnCheckedChangeListener { _, b ->
            with(GlobalApplication.pref){
                when(b){
                    true -> {this.settingSetInt("tempSave", 1)}
                    false -> {
                        this.settingSetInt("tempSave", 0)
                        this.writeSetBoolean("saveCheck", false)
                    }
                }
            }
        }
    }
}