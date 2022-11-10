package com.tussle.angrycontrol.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.DiaryShowLayoutBinding
import com.tussle.angrycontrol.model.DB.Repo
import com.tussle.angrycontrol.model.DB.RepoFactory
import com.tussle.angrycontrol.model.DateAndDiary
import com.tussle.angrycontrol.viewmodel.DiaryShowViewModel
import java.lang.IllegalArgumentException
import java.nio.file.Path
import java.time.format.DateTimeFormatter
import java.util.*

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
        val intent = intent
        viewModel.setDiaryInfo(intent.getSerializableExtra("info") as DateAndDiary)
        init()
    }
    private fun init(){
        setButton()
        setSpinner()
        setObserver()
    }
    private fun setButton(){
        binding.diaryShowBackButton.setOnClickListener {
            finish()
        }
    }
    private fun setSpinner(){
        ArrayAdapter.createFromResource(
            this,
        R.array.diaryShowSpinnerMenu,
        R.layout.diary_show_spinner_select)
            .also { adapter ->
                adapter.setDropDownViewResource(R.layout.diary_show_spinner_item)
                binding.diaryShowSpinner.adapter = adapter
            }
        binding.diaryShowSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                when(position) {
                    0 -> {}
                    1 -> {}
                    else -> throw IllegalArgumentException("Not Found Spinner Menu ID")
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }
    private fun setObserver(){
        viewModel.DiaryInfo.observe(this){
            binding.diaryShowContent.text = it.angryDiary.content
            when(it.angryDate.angryDegree) {
                1 -> animationStart(binding.diaryShowAngryIcon1)
                2 -> animationStart(binding.diaryShowAngryIcon2)
                3 -> animationStart(binding.diaryShowAngryIcon3)
                4 -> animationStart(binding.diaryShowAngryIcon4)
                5 -> animationStart(binding.diaryShowAngryIcon5)
                else -> throw IllegalArgumentException("Not Found Angry Degree")
            }
            binding.diaryShowDate.text = it.angryDate.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }
    private fun animationStart(icon : View){
        val anim = AnimationUtils.loadAnimation(this,R.anim.rotate)
        icon.startAnimation(anim)
    }
}