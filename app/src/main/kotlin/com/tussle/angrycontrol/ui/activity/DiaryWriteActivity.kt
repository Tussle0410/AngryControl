package com.tussle.angrycontrol.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.DiaryWriteAlertdialogBinding
import com.tussle.angrycontrol.databinding.DiaryWriteLayoutBinding
import com.tussle.angrycontrol.databinding.DiaryWriteSaveAlertdialogBinding
import com.tussle.angrycontrol.viewmodel.DiaryWriteViewModel

class DiaryWriteActivity : AppCompatActivity() {
    private val viewModel : DiaryWriteViewModel by lazy {
        ViewModelProvider(this).get(DiaryWriteViewModel::class.java)
    }
    private lateinit var binding : DiaryWriteLayoutBinding
    private lateinit var icons : Array<ImageView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.diary_write_layout)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val intent = intent
        viewModel.setKind(intent.getIntExtra("kinds", 0))
        init()
    }
    private fun init(){
        setImageIcon()
        setButton()
        setObserver()
        viewModel.setWriteDate()
        if(viewModel.writeKinds != 2)
            viewModel.setSaveCheck()
    }
    private fun setImageIcon(){
        icons = arrayOf(binding.diaryWriteAngryIcon1, binding.diaryWriteAngryIcon2,
            binding.diaryWriteAngryIcon3, binding.diaryWriteAngryIcon4, binding.diaryWriteAngryIcon5)
        icons.forEachIndexed { index, icon ->
            icon.setOnClickListener {
                viewModel.setDegree(index+1)
                animationStart(index)
                animationClear(icon)
            }
        }
    }
    private fun setButton(){
        binding.diaryWriteBackButton.setOnClickListener {
            finish()
        }
        binding.diaryWriteTempSaveButton.setOnClickListener {
            val alertBinding = DiaryWriteSaveAlertdialogBinding.inflate(LayoutInflater.from(this))
            val alertDialog = AlertDialog.Builder(this)
                .setView(alertBinding.root)
                .show()
            alertBinding.diaryWriteSaveCancelButton.setOnClickListener {
                alertDialog.cancel()
            }
            alertBinding.diaryWriteSaveConfirmButton.setOnClickListener {
                viewModel.diaryBackUp()
                alertDialog.cancel()
                finish()
            }
        }
    }
    private fun setObserver(){
        viewModel.saveCheck.observe(this, Observer {
            if(it){
                val alertBinding = DiaryWriteAlertdialogBinding.inflate(LayoutInflater.from(this))
                val alertDialog = AlertDialog.Builder(this)
                    .setView(alertBinding.root)
                    .show()
                alertBinding.diaryWriteDialogCancelButton.setOnClickListener {
                    alertDialog.cancel()
                }
                alertBinding.diaryWriteDialogConfirmButton.setOnClickListener {
                    viewModel.diaryRestore()
                    animationStart(viewModel.angryDegree-1)
                    alertDialog.cancel()
                }
            }
        })
    }
    private fun animationStart(curImage : Int){
        val anim = AnimationUtils.loadAnimation(this, R.anim.rotate)
        icons[curImage].startAnimation(anim)
    }
    private fun animationClear(curImage : View){
        icons
            .filterNot { it == curImage }
            .forEach {  image ->
                image.clearAnimation()
            }
    }
}