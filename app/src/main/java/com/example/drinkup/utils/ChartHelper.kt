package com.example.drinkup.utils


import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

object ChartHelper {

    fun setupBarChart(chart: BarChart) {
        chart.apply {
            description.isEnabled = false
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)
            setPinchZoom(false)
            setScaleEnabled(false)

            // X Axis
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textColor = Color.WHITE
                textSize = 10f
            }

            // Left Axis
            axisLeft.apply {
                setDrawGridLines(true)
                textColor = Color.WHITE
                textSize = 10f
                axisMinimum = 0f
                gridColor = Color.parseColor("#33FFFFFF")
            }

            // Right Axis
            axisRight.isEnabled = false

            // Legend
            legend.apply {
                isEnabled = false
            }

            animateY(1000)
        }
    }

    fun createBarData(entries: List<BarEntry>, labels: List<String>): BarData {
        val dataSet = BarDataSet(entries, "").apply {
            color = Color.parseColor("#4A90E2")
            valueTextColor = Color.WHITE
            valueTextSize = 10f
            setDrawValues(true)
        }

        val barData = BarData(dataSet)
        barData.barWidth = 0.8f

        return barData
    }

    fun updateChartData(
        chart: BarChart,
        entries: List<BarEntry>,
        labels: List<String>
    ) {
        val barData = createBarData(entries, labels)

        chart.apply {
            data = barData
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.labelCount = labels.size
            invalidate()
        }
    }
}