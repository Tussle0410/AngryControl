package com.tussle.angrycontrol.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
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
        init()
    }
    private fun init(){
        setImageIcon()
        setButton()
    }
    private fun setImageIcon(){
        val icons = arrayOf(binding.diaryWriteAngryIcon1, binding.diaryWriteAngryIcon2,
            binding.diaryWriteAngryIcon3, binding.diaryWriteAngryIcon4, binding.diaryWriteAngryIcon5)
        val anim = AnimationUtils.loadAnimation(this, R.anim.rotate)
        icons.forEach { icon ->
            icon.startAnimation(anim)
            animationClear(icons, icon)
        }
    }
    private fun setButton(){
        binding.diaryWriteBackButton.setOnClickListener {
            finish()
        }
    }
    private fun animationClear(imageArr : Array<ImageView>, curImage : View){
        imageArr
            .filterNot { it == curImage }
            .forEach {  image ->
                image.clearAnimation()
            }
    }
}