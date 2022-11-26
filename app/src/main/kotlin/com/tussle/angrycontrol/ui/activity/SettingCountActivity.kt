package com.tussle.angrycontrol.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.SettingCountLayoutBinding
import com.tussle.angrycontrol.sharedPreference.GlobalApplication
import com.tussle.angrycontrol.viewmodel.SettingCountViewModel
import java.lang.IllegalArgumentException

class SettingCountActivity : AppCompatActivity() {
    private val viewModel : SettingCountViewModel by lazy {
        ViewModelProvider(this).get(SettingCountViewModel::class.java)
    }
    private lateinit var binding : SettingCountLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.setting_count_layout)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        init()
    }
    private fun init(){
        setButton()
        checkRadio()
        setRadioButton()
    }
    private fun setButton(){
        binding.settingCountBackButton.setOnClickListener {
            finish()
        }
    }
    private fun checkRadio(){
        when(viewModel.countStart) {
            "10" -> binding.settingCountStartTime10.isChecked = true
            "20" -> binding.settingCountStartTime20.isChecked = true
            "30" -> binding.settingCountStartTime30.isChecked = true
            "60" -> binding.settingCountStartTime60.isChecked = true
            else -> throw IllegalArgumentException("Not Found CountStartTime")
        }
        when(viewModel.countEffect) {
            "Vibration" -> binding.settingCountCountEffectVibration.isChecked = true
            "Sound" -> binding.settingCountCountEffectSound.isChecked = true
            "Sound+Vibration" -> binding.settingCountCountEffectVibrationAndSound.isChecked = true
            "NotEffect" -> binding.settingCountCountEffectNotEffect.isChecked = true
            else -> throw IllegalArgumentException("Not Found CountEffect")
        }
    }
    private fun setRadioButton(){
        binding.settingCountStartTimeRadio.setOnCheckedChangeListener { _, i ->
            with(GlobalApplication.pref){
                when(i) {
                    R.id.setting_count_startTime_10 -> this.settingSetString("countStart", "10")
                    R.id.setting_count_startTime_20 -> this.settingSetString("countStart", "20")
                    R.id.setting_count_startTime_30 -> this.settingSetString("countStart", "30")
                    R.id.setting_count_startTime_60 -> this.settingSetString("countStart", "60")
                    else -> throw IllegalArgumentException("Not Found Count RadioButton ID")
                }
            }
        }
        binding.settingCountEffectRadio.setOnCheckedChangeListener { b , i ->
            with(GlobalApplication.pref){
                when(i) {
                    R.id.setting_count_countEffect_vibration -> this.settingSetString("countEffect", "Vibration")
                    R.id.setting_count_countEffect_sound -> this.settingSetString("countEffect", "Sound")
                    R.id.setting_count_countEffect_vibrationAndSound -> this.settingSetString("countEffect", "Sound+Vibration")
                    R.id.setting_count_countEffect_notEffect -> this.settingSetString("countEffect", "NotEffect")
                    else -> throw IllegalArgumentException("Not Found Effect RadioButton ID")
                }
            }
        }
    }
}