package com.example.drinkup.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.drinkup.viewmodel.DrinkViewModel
import com.example.drinkup.R
import com.example.drinkup.database.AppDatabase
import com.example.drinkup.database.DailySummary
import com.example.drinkup.databinding.FragmentDashboardBinding
import com.example.drinkup.utils.DateUtils
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch



class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DrinkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChart()
        observeViewModel()
        setupFab()
    }

    private fun setupChart() {
        binding.barChart.apply {
            description.isEnabled = false
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)
            setPinchZoom(false)
            setScaleEnabled(false)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textColor = Color.WHITE
                textSize = 10f
            }

            axisLeft.apply {
                setDrawGridLines(true)
                textColor = Color.WHITE
                textSize = 10f
                axisMinimum = 0f
            }

            axisRight.isEnabled = false
            legend.isEnabled = false
        }
    }

    private fun observeViewModel() {
        viewModel.todayTotal.observe(viewLifecycleOwner) { total ->
            val liters = total / 1000f
            binding.tvTodayTotal.text =
                getString(R.string.today_consumption, String.format("%.2f", liters))

            val percentage = viewModel.getProgressPercentage()
            binding.tvMotivation.text = when {
                percentage >= 100 -> getString(R.string.motivation_achieved)
                percentage >= 75 -> getString(R.string.motivation_great)
                percentage >= 50 -> getString(R.string.motivation_good)
                percentage >= 25 -> getString(R.string.motivation_start)
                else -> getString(R.string.motivation_begin)
            }
        }

        loadWeeklySummary()
    }

    private fun loadWeeklySummary() {
        viewModel.currentUser.value?.let { user ->
            lifecycleScope.launch {
                val database = AppDatabase.getDatabase(requireContext())
                val dao = database.drinkDao()
                val summary = dao.getWeeklySummary(user.id)
                updateChart(summary)
            }
        }
    }

    private fun updateChart(summary: List<DailySummary>) {
        val lastSevenDays = DateUtils.getLastSevenDays()
        val entries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()

        lastSevenDays.forEachIndexed { index, date ->
            val dayData = summary.find { it.date == date }
            val liters = (dayData?.total ?: 0) / 1000f
            entries.add(BarEntry(index.toFloat(), liters))
            labels.add(DateUtils.getDayOfWeek(date))
        }

        val dataSet = BarDataSet(entries, "").apply {
            color = Color.parseColor("#4A90E2")
            valueTextColor = Color.WHITE
            valueTextSize = 10f
        }

        binding.barChart.data = BarData(dataSet).apply { barWidth = 0.8f }
        binding.barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.barChart.invalidate()
    }

    private fun setupFab() {
        binding.fabAddDrink.setOnClickListener {
            showAddDrinkBottomSheet()
        }
    }

    private fun showAddDrinkBottomSheet() {
        val bottomSheet = BottomSheetDialog(requireContext())
        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_add_drink, null)

        val buttons = mapOf(
            R.id.btn_100ml to 100,
            R.id.btn_200ml to 200,
            R.id.btn_300ml to 300,
            R.id.btn_500ml to 500
        )

        buttons.forEach { (id, quantity) ->
            sheetView.findViewById<View>(id)?.setOnClickListener {
                viewModel.addDrink(quantity)
                bottomSheet.dismiss()
            }
        }


        sheetView.findViewById<View>(R.id.btn_custom)?.setOnClickListener {
            bottomSheet.dismiss()
            showCustomAmountDialog()
        }

        bottomSheet.setContentView(sheetView)
        bottomSheet.show()
    }
    private fun showCustomAmountDialog() {
        val input = EditText(requireContext())
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        input.hint = getString(R.string.hint_custom_amount)

        val padding = resources.getDimensionPixelSize(R.dimen.padding_medium)
        input.setPadding(padding, padding, padding, padding)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.title_custom_amount))
            .setView(input)
            .setPositiveButton(getString(R.string.add)) { _, _ ->
                val amount = input.text.toString().toIntOrNull()

                if (amount != null && amount in 1..5000) {
                    viewModel.addDrink(amount)
                    Toast.makeText(requireContext(), "Added $amount ml", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

}
