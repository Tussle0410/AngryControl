package com.tussle.angrycontrol.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tussle.angrycontrol.Event.EventObserver
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.CountCompleteDialogBinding
import com.tussle.angrycontrol.databinding.CountFrameBinding
import com.tussle.angrycontrol.model.DB.Repo
import com.tussle.angrycontrol.model.DB.RepoFactory
import com.tussle.angrycontrol.ui.activity.DiaryWriteActivity
import com.tussle.angrycontrol.viewmodel.MainViewModel

class CountFragment : Fragment() {
    private val viewModel by lazy {
        val factory = RepoFactory(Repo())
        ViewModelProvider(requireActivity(), factory).get(MainViewModel::class.java)
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
        viewModel.angryCountEvent.observe(requireActivity(), EventObserver{
            viewModel.plusId()
            viewModel.initCountAngryDegree()
        })
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
                angryImages.forEachIndexed { index, icon ->
                    icon.setOnClickListener {
                        viewModel.changeCountAngryDegree(index+1)
                        icon.startAnimation(anim)
                        animationClear(angryImages, icon)
                    }
                }
                dialogBinding.countDialogConfirm.setOnClickListener {
                    if(viewModel.countAngryDegree != 0){
                        binding.CountDownStartButton.isEnabled = true
                        alertDialog.cancel()
                        viewModel.insertAngryDate()
                        if(dialogBinding.countWriteRadio.isChecked){
                            val intent = Intent(requireContext(), DiaryWriteActivity::class.java)
                            intent.putExtra("kinds", 2)
                            startActivity(intent)
                        }
                    }else
                        Toast.makeText(requireContext(), "분노 정도를 선택해주세요.",Toast.LENGTH_SHORT).show()
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