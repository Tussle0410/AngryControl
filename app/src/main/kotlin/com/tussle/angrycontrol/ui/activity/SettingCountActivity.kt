package com.tussle.angrycontrol.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.SettingCountLayoutBinding
import com.tussle.angrycontrol.viewmodel.SettingCountViewModel

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
    }
}