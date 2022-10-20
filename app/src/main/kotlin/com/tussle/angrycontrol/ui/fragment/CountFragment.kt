package com.tussle.angrycontrol.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.CountFrameBinding
import com.tussle.angrycontrol.sharedPreference.GlobalApplication
import com.tussle.angrycontrol.viewmodel.MainViewModel

class CountFragment : Fragment() {
    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }
    private lateinit var binding : CountFrameBinding
    private lateinit var countTimer : CountDownTimer
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.count_frame, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        init()
        return binding.root
    }
    private fun init(){
        countTimeSetting()
        setObserver()
        buttonSetting()
    }
    private fun setObserver(){
        viewModel.countDownStartTime.observe(requireActivity(), Observer {
            timerSetting(it)
        })
    }
    private fun countTimeSetting(){
        viewModel.setCountStartTime(GlobalApplication.pref.settingGetString("startTime", "10"))
    }
    private fun buttonSetting(){
        binding.CountDownStartButton.setOnClickListener {
            countTimer.start()
        }
    }
    private fun timerSetting(time : String){
        countTimer = object : CountDownTimer(viewModel.stringToMillisSecond(time), 1000L){
            override fun onTick(p0: Long) {
                binding.CountDownText.text = viewModel.millisSecondToSecond(p0)
            }
            override fun onFinish() {
                binding.CountDownText.text = viewModel.countDownStartTime.value
            }
        }
    }
    companion object{
        fun getInstance() : CountFragment
            = CountFragment()
    }
}