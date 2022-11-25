package com.tussle.angrycontrol.ui.formatter

import com.github.mikephil.charting.formatter.ValueFormatter

class PieChartFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "${value.toInt()}번"
    }

}