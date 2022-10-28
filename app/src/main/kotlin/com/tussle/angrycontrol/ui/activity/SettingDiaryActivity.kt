package com.tussle.angrycontrol.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.SettingDiaryLayoutBinding
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
    }
}