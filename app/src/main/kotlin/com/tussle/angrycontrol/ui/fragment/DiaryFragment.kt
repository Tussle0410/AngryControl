package com.tussle.angrycontrol.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.DiaryDateAlterdialogBinding
import com.tussle.angrycontrol.databinding.DiaryFrameBinding
import com.tussle.angrycontrol.model.DateAndDiary
import com.tussle.angrycontrol.ui.activity.DiaryShowActivity
import com.tussle.angrycontrol.ui.activity.DiaryWriteActivity
import com.tussle.angrycontrol.ui.adapter.DiaryRecyclerAdapter
import com.tussle.angrycontrol.ui.listener.DiaryCallBackListener
import com.tussle.angrycontrol.viewmodel.MainViewModel
import java.io.Serializable
import java.time.LocalDateTime

class DiaryFragment : Fragment(), DiaryCallBackListener {
    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }
    private lateinit var binding : DiaryFrameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.diary_frame, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        init()
        return binding.root
    }

    private fun init(){
        setButton()
        setObserver()
    }
    private fun setAdapter(){
        viewModel.setDiaryList()
        with(binding.diaryRecyclerView){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = DiaryRecyclerAdapter(viewModel.angryDiary, requireContext(), this@DiaryFragment)
        }
    }
    private fun setButton(){
        binding.diaryWriteButton.setOnClickListener {
            val intent = Intent(requireContext(), DiaryWriteActivity::class.java)
            intent.putExtra("kinds", 1)
            intent.putExtra("id", viewModel.getIdCount())
            startActivity(intent)
        }
        binding.diaryTimeSelectButton.setOnClickListener {
            val dialogBinding = DiaryDateAlterdialogBinding.inflate(LayoutInflater.from(requireContext()))
            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(dialogBinding.root)
                .show()
            with(dialogBinding){
                diaryDialogYearNumberPicker.minValue = 2022
                diaryDialogYearNumberPicker.maxValue = 2030
                diaryDialogMonthNumberPicker.minValue = 1
                diaryDialogMonthNumberPicker.maxValue = 12
                diaryDialogSaveButton.setOnClickListener {
                    val start = LocalDateTime.of(diaryDialogYearNumberPicker.value, diaryDialogMonthNumberPicker.value,
                        1, 0, 0)
                    val end = start.plusMonths(1)
                    binding.diaryTimeSelectButton.text = "${diaryDialogYearNumberPicker.value}년 ${diaryDialogMonthNumberPicker.value}월"
                    viewModel.setDiaryCondition(false, start, end)
                    setAdapter()
                    alertDialog.cancel()
                }
                diaryDialogWholeButton.setOnClickListener {
                    binding.diaryTimeSelectButton.text = requireContext().getString(R.string.whole)
                    viewModel.setDiaryCondition(true, null, null)
                    setAdapter()
                    alertDialog.cancel()
                }
                diaryDialogCancelButton.setOnClickListener {
                    alertDialog.cancel()
                }
            }
        }
    }
    private fun setObserver(){
        viewModel.angryDateAndDiary.observe(requireActivity()) {
            setAdapter()
        }
    }
    override fun diaryShowIntent(info: DateAndDiary) {
        val intent = Intent(requireContext(), DiaryShowActivity::class.java).apply {
            putExtra("info", info as Serializable)
        }
        startActivity(intent)
    }
    companion object{
        fun getInstance() : DiaryFragment
            = DiaryFragment()
    }
}