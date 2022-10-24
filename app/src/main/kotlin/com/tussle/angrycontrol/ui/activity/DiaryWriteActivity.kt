package com.tussle.angrycontrol.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.DiaryWriteLayoutBinding
import com.tussle.angrycontrol.viewmodel.DiaryWriteViewModel

class DiaryWriteActivity : AppCompatActivity() {
    private val viewModel : DiaryWriteViewModel by lazy {
        ViewModelProvider(this).get(DiaryWriteViewModel::class.java)
    }
    private lateinit var binding : DiaryWriteLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = DataBindingUtil.setContentView(this, R.layout.diary_write_layout)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}