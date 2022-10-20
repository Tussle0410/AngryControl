package com.tussle.angrycontrol.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import com.tussle.angrycontrol.viewmodel.MainViewModel
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class CalendarFragment : Fragment() {
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
    }
    private fun calendarSetting(){
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        with(binding.calendarView){
            setup(firstMonth, lastMonth,firstDayOfWeek)
            scrollToMonth(currentMonth)
        }
        class DayViewContainer(view : View) : ViewContainer(view){
            val dayText = CalendarDayLayoutBinding.bind(view).calendarDayText
        }
        class MonthHeaderViewContainer(view : View) : ViewContainer(view){
            val yearMonth = CalendarHeadLayoutBinding.bind(view).monthAndYear
        }
        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer>{
            override fun create(view: View): DayViewContainer
                = DayViewContainer(view)

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.dayText.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH)
                    container.dayText.setTextColor(Color.BLACK)
                else
                    container.dayText.setTextColor(Color.GRAY)
            }
        }
        binding.calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthHeaderViewContainer>{
            override fun create(view: View): MonthHeaderViewContainer
                = MonthHeaderViewContainer(view)

            override fun bind(container: MonthHeaderViewContainer, month: CalendarMonth) {
                container.yearMonth.text = month.yearMonth.toString()
            }
        }
    }
    companion object{
        fun getInstance() : CalendarFragment
            = CalendarFragment()
    }
}