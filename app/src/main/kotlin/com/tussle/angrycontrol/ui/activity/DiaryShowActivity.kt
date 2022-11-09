package com.tussle.angrycontrol.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.DiaryShowLayoutBinding
import com.tussle.angrycontrol.model.DB.Repo
import com.tussle.angrycontrol.model.DB.RepoFactory
import com.tussle.angrycontrol.viewmodel.DiaryShowViewModel

class DiaryShowActivity : AppCompatActivity() {
    private val viewModel : DiaryShowViewModel by lazy {
        val factory = RepoFactory(Repo())
        ViewModelProvider(this, factory).get(DiaryShowViewModel::class.java)
    }
    private lateinit var binding : DiaryShowLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.diary_show_layout)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        init()
    }
    private fun init(){

    }
}