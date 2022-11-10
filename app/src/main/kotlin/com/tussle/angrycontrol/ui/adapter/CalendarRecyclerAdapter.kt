package com.tussle.angrycontrol.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tussle.angrycontrol.databinding.CalendarRecyclerItemBinding
import com.tussle.angrycontrol.model.DateAndDiary

class CalendarRecyclerAdapter(private val data : MutableList<DateAndDiary>) : RecyclerView.Adapter<CalendarRecyclerAdapter.CalendarViewHolder>() {
    inner class CalendarViewHolder(private val binding : CalendarRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun setting(info : DateAndDiary){
            binding.calendarItemText.text = "text"
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarRecyclerAdapter.CalendarViewHolder {
        val binding = CalendarRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarRecyclerAdapter.CalendarViewHolder, position: Int) {
        holder.setting(data[position])
    }

    override fun getItemCount() = data.size

}