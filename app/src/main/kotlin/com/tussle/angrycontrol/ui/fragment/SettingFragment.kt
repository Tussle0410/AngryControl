package com.tussle.angrycontrol.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
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
    private var mInterstitialAd: InterstitialAd? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.setting_frame,container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        init()
        return binding.root
    }
    private fun init(){
        setAdMob()
        setButton()
        setTextView()
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
        binding.ADSettingButton.setOnClickListener {
            showAdMob()
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
    private fun setTextView(){
        val count = viewModel.getAngryCount()
        if(count == null)
            binding.settingAngryCountText.text = "0"
        else
            binding.settingAngryCountText.text = viewModel.getAngryCount().toString()
    }
    private fun setAdMob(){
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),"ca-app-pub-3940256099942544/1033173712",adRequest,object:InterstitialAdLoadCallback(){
            override fun onAdLoaded(ad: InterstitialAd) {
                super.onAdLoaded(ad)
                mInterstitialAd = ad
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                super.onAdFailedToLoad(error)
                mInterstitialAd = null
            }
        })
    }
    private fun showAdMob(){
        if(mInterstitialAd != null)
            mInterstitialAd?.show(requireActivity())
        else
            Toast.makeText(requireContext(), "광고가 준비되지 않았습니다.",Toast.LENGTH_SHORT).show()
    }
    companion object{
        fun getInstance() : SettingFragment
            = SettingFragment()
    }
}