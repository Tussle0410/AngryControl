package com.tussle.angrycontrol.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.SettingFrameBinding
import com.tussle.angrycontrol.ui.activity.SettingBackUpActivity
import com.tussle.angrycontrol.ui.activity.SettingCountActivity
import com.tussle.angrycontrol.ui.activity.SettingDiaryActivity
import com.tussle.angrycontrol.viewmodel.MainViewModel

class SettingFragment : Fragment() {
    private val viewModel : MainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }
    private lateinit var binding : SettingFrameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.setting_frame,container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        init()
        return binding.root
    }
    private fun init(){
        setButton()
    }
    private fun setButton(){
        binding.countSettingButton.setOnClickListener {
            val intent = Intent(requireContext(),SettingCountActivity::class.java)
            startActivity(intent)
        }
        binding.diarySettingButton.setOnClickListener {
            val intent = Intent(requireContext(), SettingDiaryActivity::class.java)
            startActivity(intent)
        }
        binding.backUpSettingButton.setOnClickListener {
            val intent = Intent(requireContext(), SettingBackUpActivity::class.java)
            startActivity(intent)
        }
        binding.inquirySettingButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "plane/text"
                val address = arrayOf("cksgud410@gmail.com")
                putExtra(Intent.EXTRA_EMAIL, address)
                putExtra(Intent.EXTRA_SUBJECT,"분노 진정 어플리케이션 문의하기")
            }
            startActivity(intent)
        }
    }
    companion object{
        fun getInstance() : SettingFragment
            = SettingFragment()
    }
}