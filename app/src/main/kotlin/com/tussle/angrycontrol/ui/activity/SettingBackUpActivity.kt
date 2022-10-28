package com.tussle.angrycontrol.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.SettingBackupLayoutBinding
import com.tussle.angrycontrol.viewmodel.SettingBackUpViewModel

class SettingBackUpActivity : AppCompatActivity() {
    private val viewModel : SettingBackUpViewModel by lazy {
        ViewModelProvider(this).get(SettingBackUpViewModel::class.java)
    }
    private lateinit var binding : SettingBackupLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.setting_backup_layout)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}