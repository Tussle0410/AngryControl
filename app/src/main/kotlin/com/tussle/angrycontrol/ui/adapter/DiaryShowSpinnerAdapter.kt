package com.tussle.angrycontrol.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tussle.angrycontrol.databinding.DiaryShowSpinnerItemBinding
import com.tussle.angrycontrol.databinding.DiaryShowSpinnerSelectBinding

class DiaryShowSpinnerAdapter(
    context : Context,
    @LayoutRes private val resId : Int,
    data : Array<String>)
    : ArrayAdapter<String>(context, resId, data) {
    private val mContext = context
    private val mData = data

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = DiaryShowSpinnerSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        if(position == 0)
            return initialSelection(true)
        val binding = DiaryShowSpinnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.spinnerText.text = mData[position]
        return binding.root
    }

    override fun getItem(position: Int): String?
        =mData[position]

    override fun getCount(): Int
        = mData.size
    private fun initialSelection(dropdown : Boolean) : View {
        // Just an example using a simple TextView. Create whatever default view
        // to suit your needs, inflating a separate layout if it's cleaner.
        val view = TextView(mContext)
        view.text = " "
        view.setPadding(0, 0, 0, 0)

        if (dropdown) { // Hidden when the dropdown is opened
            view.height = 0
        }

        return view;
    }
}