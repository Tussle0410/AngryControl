package com.tussle.angrycontrol.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.tussle.angrycontrol.R
import com.tussle.angrycontrol.databinding.CalendarDayLayoutBinding
import com.tussle.angrycontrol.databinding.CalendarFrameBinding
import com.tussle.angrycontrol.databinding.CalendarHeadLayoutBinding
import com.tussle.angrycontrol.model.DateAndDiary
import com.tussle.angrycontrol.ui.activity.DiaryShowActivity
import com.tussle.angrycontrol.ui.adapter.CalendarRecyclerAdapter
import com.tussle.angrycontrol.ui.adapter.DiaryRecyclerAdapter
import com.tussle.angrycontrol.ui.listener.DiaryCallBackListener
import com.tussle.angrycontrol.viewmodel.MainViewModel
import java.io.Serializable
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class CalendarFragment : Fragment(), DiaryCallBackListener {
    private val viewModel : MainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }
    private lateinit var binding : CalendarFrameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.calendar_frame, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()
        init()
        return binding.root
    }
    private fun init(){
        calendarSetting()
        val start = LocalDateTime.now()
        val end = start.plusDays(1)
        recyclerSetting(viewModel.setChartDiary(start, end))
    }
    private fun recyclerSetting(list : MutableList<DateAndDiary>){
        with(binding.calendarRecycler){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = DiaryRecyclerAdapter(list, requireContext(), this@CalendarFragment)
        }
    }
    private fun calendarSetting(){
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        with(binding.calendarCalendarView){
            setup(firstMonth, lastMonth,firstDayOfWeek)
            scrollToMonth(currentMonth)
        }
        class DayViewContainer(view : View) : ViewContainer(view){
            val dayText = CalendarDayLayoutBinding.bind(view).calendarDayText
        }
        class MonthHeaderViewContainer(view : View) : ViewContainer(view){
            val yearMonth = CalendarHeadLayoutBinding.bind(view).monthAndYear
        }
        binding.calendarCalendarView.dayBinder = object : DayBinder<DayViewContainer>{
            override fun create(view: View): DayViewContainer
                = DayViewContainer(view)

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.dayText.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH)
                    container.dayText.setTextColor(Color.BLACK)
                else
                    container.dayText.setTextColor(Color.GRAY)
                val curYear = day.date.year
                val curMonth = day.date.month
                val curDay = day.date.dayOfMonth
                val start = LocalDateTime.of(curYear, curMonth, curDay,
                        0, 0, 0)
                val end = start.plusDays(1)
                val list = viewModel.setChartDiary(start, end)
                if(list.isNotEmpty())
                    container.dayText.setBackgroundColor(requireContext().resources.getColor(R.color.main_subColor))
                else
                    container.dayText.setBackgroundColor(requireContext().resources.getColor(R.color.white))
                container.dayText.setOnClickListener {
                    recyclerSetting(list)
                    binding.calendarAngryDateText.text = "${curYear}년 ${curMonth}월 ${curDay}일에는,"
                    binding.calendarAngryCountText.text = "${list.size}번 분노!"
                }
            }
        }
        binding.calendarCalendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthHeaderViewContainer>{
            override fun create(view: View): MonthHeaderViewContainer
                = MonthHeaderViewContainer(view)

            override fun bind(container: MonthHeaderViewContainer, month: CalendarMonth) {
                container.yearMonth.text = month.yearMonth.toString()
            }
        }
    }
    override fun DiaryShowIntent(info: DateAndDiary) {
        val intent = Intent(requireContext(), DiaryShowActivity::class.java).apply {
            putExtra("info", info as Serializable)
        }
        startActivity(intent)
    }
    companion object{
        fun getInstance() : CalendarFragment
            = CalendarFragment()
    }
}