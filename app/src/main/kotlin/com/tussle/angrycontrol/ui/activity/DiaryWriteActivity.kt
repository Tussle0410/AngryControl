package com.tussle.angrycontrol.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.Event.EventObserver
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.DiaryWriteAlertdialogBinding
import com.tussle.angrycontrol.databinding.DiaryWriteLayoutBinding
import com.tussle.angrycontrol.databinding.DiaryWriteSaveAlertdialogBinding
import com.tussle.angrycontrol.model.DB.Repo
import com.tussle.angrycontrol.model.DB.RepoFactory
import com.tussle.angrycontrol.model.DateAndDiary
import com.tussle.angrycontrol.viewmodel.DiaryWriteViewModel

class DiaryWriteActivity : AppCompatActivity() {
    private val viewModel : DiaryWriteViewModel by lazy {
        val factory = RepoFactory(Repo())
        ViewModelProvider(this, factory).get(DiaryWriteViewModel::class.java)
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
        if(viewModel.writeKinds==2)
            viewModel.setDegree(intent.getIntExtra("degree", 0))
        else{
            val info = intent.getSerializableExtra("info") as DateAndDiary
        }
        init()
    }
    private fun init(){
        setImageIcon()
        setButton()
        setObserver()
        if(viewModel.writeKinds == 2){
            viewModel.setWriteDate(true)
            viewModel.getCountDiaryCheck(false)
            animationStart(viewModel.angryDegree - 1)
        }else if(viewModel.writeKinds == 1)
            viewModel.getSaveCheck()
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
        binding.diaryWriteSaveButton.setOnClickListener {
            viewModel.insertDiary()
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
                    viewModel.setWriteDate(true)
                    alertDialog.cancel()
                }
                alertBinding.diaryWriteDialogConfirmButton.setOnClickListener {
                    viewModel.diaryRestore()
                    animationStart(viewModel.angryDegree-1)
                    viewModel.setWriteDate(false)
                    viewModel.setCurSave()
                    alertDialog.cancel()
                }
            }else
                viewModel.setWriteDate(true)
        })
        viewModel.insertEvent.observe(this, EventObserver{
            if(it){
                viewModel.plusId()
                viewModel.setSaveCheck()
                viewModel.setCountDiaryCheck()
                finish()
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