package com.tussle.angrycontrol.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.DiaryFrameBinding
import com.tussle.angrycontrol.ui.activity.DiaryWriteActivity
import com.tussle.angrycontrol.ui.adapter.DiaryRecyclerAdapter
import com.tussle.angrycontrol.viewmodel.MainViewModel

class DiaryFragment : Fragment() {
    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }
    private lateinit var binding : DiaryFrameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.diary_frame, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        init()
        return binding.root
    }
    private fun init(){
        setAdapter()
        setButton()
    }
    private fun setAdapter(){
        val list = mutableListOf("일기1", "일기2", "일기3")
        with(binding.diaryRecyclerView){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = DiaryRecyclerAdapter(list)
        }
    }
    private fun setButton(){
        binding.diaryWriteButton.setOnClickListener {
            val intent = Intent(requireContext(), DiaryWriteActivity::class.java)
            startActivity(intent)
        }
    }
    companion object{
        fun getInstance() : DiaryFragment
            = DiaryFragment()
    }
}