package com.tussle.angrycontrol.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tussle.angrycontrol.databinding.DiaryRecyclerItemBinding

class DiaryRecyclerAdapter(private val data : MutableList<String>) : RecyclerView.Adapter<DiaryRecyclerAdapter.DiaryViewHolder>() {
    inner class DiaryViewHolder(private val binding : DiaryRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun setting(text : String){
            binding.diaryItemText.text = text
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