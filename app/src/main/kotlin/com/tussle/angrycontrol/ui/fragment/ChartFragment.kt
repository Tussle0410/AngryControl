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
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.ChartFrameBinding
import com.tussle.angrycontrol.model.DB.Repo
import com.tussle.angrycontrol.model.DB.RepoFactory
import com.tussle.angrycontrol.ui.formatter.PieChartFormatter
import com.tussle.angrycontrol.ui.renderer.CustomPieChartRenderer
import com.tussle.angrycontrol.viewmodel.MainViewModel
import java.time.LocalDateTime

class ChartFragment : Fragment() {
    private val viewModel : MainViewModel by lazy {
        val factory = RepoFactory(Repo())
        ViewModelProvider(requireActivity(), factory).get(MainViewModel::class.java)
    }
    private lateinit var binding : ChartFrameBinding
    private var entries = ArrayList<PieEntry>()
    private lateinit var pieChartColor  : MutableList<Int>
    private lateinit var angryName : Array<String>
    private lateinit var chartConditionButtons : List<Button>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.chart_frame, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        angryName = requireContext().resources.getStringArray(R.array.angryDegreeName)
        pieChartColor = resources.getIntArray(R.array.pieChartColor).toMutableList()
        init()
        return binding.root
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            setChart()
        }
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
            val todayDateE = LocalDateTime.now()
            val todayDateS = todayDateE.minusYears(1)
            viewModel.setChartCondition(false,todayDateS,todayDateE)
            setChart()
        }
        binding.chartMonthButton.setOnClickListener {
            selectConditionButton(2)
            val todayDateE = LocalDateTime.now()
            val todayDateS = todayDateE.minusMonths(1)
            viewModel.setChartCondition(false,todayDateS,todayDateE)
            setChart()
        }
        binding.chartWeekendButton.setOnClickListener {
            selectConditionButton(3)
            val todayDateE = LocalDateTime.now()
            val todayDateS = todayDateE.minusWeeks(1)
            viewModel.setChartCondition(false,todayDateS,todayDateE)
            setChart()
        }
    }
    private fun setEntries(){
        viewModel.setDegreeCount()
        viewModel.setChartDegree()
        entries.clear()
        var max = -1
        var id = -1
        for(i in 0..4){
            entries.add(PieEntry(viewModel.degreeCount[i].toFloat(), angryName[i]))
            if(viewModel.degreeCount[i] > max){
                max = viewModel.degreeCount[i]
                id = i
            }
        }
        if(max != 0)
            animationStart(id)
    }
    private fun setChart(){
        setEntries()
        val pieDataSet = PieDataSet(entries,"").apply {
            colors = pieChartColor     //PieChart Colors
            valueLinePart1Length = 0.6f        //ValueLine1(-)
            valueLinePart2Length = 0.3f         //ValueLine2(/)
            valueLineWidth = 2f     //ValueLine Width
            valueLinePart1OffsetPercentage = 115f
            isUsingSliceColorAsValueLineColor = true    //PieChartColor == ValueLineColor
            yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE     //ValueLine Position OutSide
            valueTypeface = Typeface.DEFAULT_BOLD      //valueText Bold
            valueFormatter = PieChartFormatter()
            valueTextSize = 20f
            selectionShift = 3f
        }
        val pieData = PieData(pieDataSet).apply {
            setValueTextColors(pieChartColor)
        }
        with(binding.pieChart){
            data = pieData
            setExtraOffsets(20f, 20f, 20f, 20f)
            renderer = CustomPieChartRenderer(this,10f)
            setUsePercentValues(false)
            description.isEnabled = false       //description InVisible
            setDrawEntryLabels(false)
            isDrawHoleEnabled = true
            holeRadius = 50f
            setHoleColor(requireContext().resources.getColor(R.color.main_subColor))      //PieChart Center Hole Color
            animateY(1000, Easing.EaseInCubic)
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
        for(index in 0..3){
            with(chartConditionButtons[index]){
                if(index == check){
                    isEnabled = false
                    setBackgroundColor(requireContext().resources.getColor(R.color.main_color))
                    setTextColor(requireContext().resources.getColor(R.color.white))
                }else{
                    isEnabled = true
                    setBackgroundColor(requireContext().resources.getColor(R.color.main_subColor))
                    setTextColor(requireContext().resources.getColor(R.color.main_color))
                }
            }
        }
    }
    companion object{
        fun getInstance() : ChartFragment
            = ChartFragment()
    }
}