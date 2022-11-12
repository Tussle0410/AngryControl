package com.tussle.angrycontrol.ui.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.ChartFrameBinding
import com.tussle.angrycontrol.model.DB.Repo
import com.tussle.angrycontrol.model.DB.RepoFactory
import com.tussle.angrycontrol.viewmodel.MainViewModel
import java.time.LocalDateTime

class ChartFragment : Fragment() {
    private val viewModel : MainViewModel by lazy {
        val factory = RepoFactory(Repo())
        ViewModelProvider(requireActivity(), factory).get(MainViewModel::class.java)
    }
    private lateinit var binding : ChartFrameBinding
    private var entries = ArrayList<PieEntry>()
    private var pieChartColor = mutableListOf<Int>()
    private val angryName = requireActivity().resources.getStringArray(R.array.angryDegreeName)
    private lateinit var chartConditionButtons : List<Button>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.chart_frame, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        init()
        return binding.root
    }
    private fun init(){
        setButton()
        setChart()
        setConditionButtons()
    }
    private fun setButton(){
        binding.chartWholeButton.setOnClickListener {
            selectConditionButton(0)
            viewModel.setChartCondition(true,null,null)
            setChart()
        }
        binding.chartYearButton.setOnClickListener {
            selectConditionButton(1)
            val todayDateS = LocalDateTime.now()
            val todayDateE = todayDateS.plusYears(1)
            viewModel.setChartCondition(false,todayDateS,todayDateE)
            setChart()
        }
        binding.chartMonthButton.setOnClickListener {
            selectConditionButton(2)
            val todayDateS = LocalDateTime.now()
            val todayDateE = todayDateS.plusMonths(1)
            viewModel.setChartCondition(false,todayDateS,todayDateE)
            setChart()
        }
        binding.chartWeekendButton.setOnClickListener {
            selectConditionButton(3)
            val todayDateS = LocalDateTime.now()
            val todayDateE = todayDateS.plusWeeks(1)
            viewModel.setChartCondition(false,todayDateS,todayDateE)
            setChart()
        }
    }
    private fun setEntries(){
        viewModel.setDegreeCount()
        viewModel.setChartDegree()
        var max = -1
        var id = -1
        for(i in 0..4){
            entries.add(PieEntry(viewModel.degreeCount[i].toFloat(), angryName[i]))
            if(viewModel.degreeCount[i] > max){
                max = viewModel.degreeCount[i]
                id = i
            }
        }
        animationStart(id)
    }
    private fun setPieChartColor(){
        pieChartColor = resources.getIntArray(R.array.pieChartColor).toMutableList()
    }
    private fun setChart(){
        setEntries()
        setPieChartColor()
        val pieDataSet = PieDataSet(entries,"").apply {
            colors = pieChartColor     //PieChart Colors
            valueLinePart1Length = 0.6f        //ValueLine1(-)
            valueLinePart2Length = 0.3f         //ValueLine2(/)
            valueLineWidth = 2f     //ValueLine Width
            valueLinePart1OffsetPercentage = 115f
            isUsingSliceColorAsValueLineColor = true    //PieChartColor == ValueLineColor
            yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE     //ValueLine Position OutSide
            valueTextSize = 16f     //valueTextSize
            valueTypeface = Typeface.DEFAULT_BOLD       //valueText Bold
        }
        val pieData = PieData(pieDataSet).apply {
            setValueTextSize(20f)
            setValueTextColor(Color.BLACK)
        }
        with(binding.pieChart){
            setUsePercentValues(false)
            description.isEnabled = false       //description InVisible
            setExtraOffsets(25f, 25f, 25f, 25f)
            dragDecelerationFrictionCoef = 0.95f
            transparentCircleRadius = 61f
            setEntryLabelTextSize(20f)
            setDrawEntryLabels(false)
            setHoleColor(Color.WHITE)       //PieChart Center Hole Color
            data = pieData
            invalidate()
        }
    }
    private fun animationStart(index : Int){
        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate)
        when(index){
            0 -> binding.chartAngry1Image.startAnimation(anim)
            1 -> binding.chartAngry2Image.startAnimation(anim)
            2 -> binding.chartAngry3Image.startAnimation(anim)
            3 -> binding.chartAngry4Image.startAnimation(anim)
            4 -> binding.chartAngry5Image.startAnimation(anim)
            else -> throw IllegalArgumentException("Not Found Angry Degree")
        }
    }
    private fun setConditionButtons(){
        chartConditionButtons = listOf(binding.chartWholeButton, binding.chartYearButton,
            binding.chartMonthButton, binding.chartWeekendButton)
    }
    private fun selectConditionButton(check : Int){
        for(index in 0..4){
            with(chartConditionButtons[index]){
                if(index == check){
                    isEnabled = false
                    setBackgroundColor(requireContext().resources.getColor(R.color.main_color))
                }else{
                    isEnabled = true
                    setBackgroundColor(requireContext().resources.getColor(R.color.main_subColor))
                }
            }
        }
    }
    companion object{
        fun getInstance() : ChartFragment
            = ChartFragment()
    }
}