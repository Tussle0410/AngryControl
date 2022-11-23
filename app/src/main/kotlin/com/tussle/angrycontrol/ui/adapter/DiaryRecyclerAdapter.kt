package com.tussle.angrycontrol.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.DiaryRecyclerItemBinding
import com.tussle.angrycontrol.model.DateAndDiary
import com.tussle.angrycontrol.ui.listener.DiaryCallBackListener
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class DiaryRecyclerAdapter(private val data : MutableList<DateAndDiary>,
    context : Context, callBack : DiaryCallBackListener) : RecyclerView.Adapter<DiaryRecyclerAdapter.DiaryViewHolder>() {
    private val mContext = context
    private val mCallBack = callBack
    private val dateFormat = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
    private val timeFormat = DateTimeFormatter.ofPattern("a hh:mm:ss", Locale.KOREA)
    inner class DiaryViewHolder(private val binding : DiaryRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun setting(info : DateAndDiary){
            binding.diaryItemLayout.setOnClickListener {
                mCallBack.diaryShowIntent(info)
            }
            binding.diaryItemText.text = info.angryDiary!!.content
            when(info.angryDate.angryDegree) {
                1 -> binding.diaryItemIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_angry1))
                2 -> binding.diaryItemIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_angry2))
                3 -> binding.diaryItemIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_angry3))
                4 -> binding.diaryItemIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_angry4))
                5 -> binding.diaryItemIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_angry5))
                else -> throw IllegalArgumentException("Not Found AngryDegree")
            }
            with(info.angryDate.date){
                binding.diaryItemDate.text = this.format(dateFormat)
                binding.diaryItemDay.text = this.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
                binding.diaryItemTime.text = this.format(timeFormat)
            }
        }
    }
    override fun onBindViewHolder(holder: DiaryRecyclerAdapter.DiaryViewHolder, position: Int) {
        holder.setting(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryRecyclerAdapter.DiaryViewHolder {
        val binding = DiaryRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return DiaryViewHolder(binding)
    }

    override fun getItemCount() = data.size
}