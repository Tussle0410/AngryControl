package com.tussle.angrycontrol.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.CountCompleteDialogBinding
import com.tussle.angrycontrol.databinding.CountFrameBinding
import com.tussle.angrycontrol.viewmodel.MainViewModel

class CountFragment : Fragment() {
    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }
    private lateinit var binding : CountFrameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.count_frame, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        init()
        return binding.root
    }
    private fun init(){
        setObserver()
        buttonSetting()
    }
    private fun setObserver(){

    }
    private fun buttonSetting(){
        binding.CountDownStartButton.setOnClickListener {
            timerSetting()
            binding.CountDownStartButton.isEnabled = false
        }
    }
    private fun timerSetting(){
        viewModel.setCountStartTime()
        object : CountDownTimer(viewModel.stringToMillisSecond(), 1000L){
            override fun onTick(p0: Long) {
                binding.CountDownText.text = viewModel.millisSecondToSecond(p0)
            }
            override fun onFinish() {
                val dialogBinding = CountCompleteDialogBinding.inflate(LayoutInflater.from(requireContext()))
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setView(dialogBinding.root)
                    .show()
                val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate)
                val angryImages = arrayOf(
                    dialogBinding.dialogAngry1, dialogBinding.dialogAngry2,
                    dialogBinding.dialogAngry3, dialogBinding.dialogAngry4,
                    dialogBinding.dialogAngry5)
                val clickListener = View.OnClickListener { image ->
                    image!!.startAnimation(anim)
                    animationClear(angryImages, image)
                }
                angryImages.forEach {
                    it.setOnClickListener(clickListener)
                }
                dialogBinding.countDialogConfirm.setOnClickListener {
                    if(viewModel.countAngryDegree != 0){
                        binding.CountDownStartButton.isEnabled = true
                        alertDialog.cancel()
                    }
                }
                dialogBinding.countDialogReplay.setOnClickListener {
                    alertDialog.cancel()
                    timerSetting()
                }
            }
        }.start()
    }
    private fun animationClear(imageArr : Array<ImageView>, curImage : View){
        imageArr
            .filterNot { it == curImage }
            .forEach { image ->
                image.clearAnimation()
            }
    }
    companion object{
        fun getInstance() : CountFragment
            = CountFragment()
    }
}