package com.example.drinkup.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drinkup.R
import com.example.drinkup.databinding.FragmentTipsBinding

class TipsFragment : Fragment() {
    private var _binding: FragmentTipsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val tips = getTipsList()
        val adapter = TipsAdapter(tips) { tip ->
            shareTip(tip)
        }

        binding.rvTips.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }

    private fun getTipsList(): List<Tip> {
        return listOf(
            Tip(
                getString(R.string.tip_1_title),
                getString(R.string.tip_1_description),
                R.drawable.ic_water_glass
            ),
            Tip(
                getString(R.string.tip_2_title),
                getString(R.string.tip_2_description),
                R.drawable.ic_alarm
            ),
            Tip(
                getString(R.string.tip_3_title),
                getString(R.string.tip_3_description),
                R.drawable.ic_bottle
            ),
            Tip(
                getString(R.string.tip_4_title),
                getString(R.string.tip_4_description),
                R.drawable.ic_food
            ),
            Tip(
                getString(R.string.tip_5_title),
                getString(R.string.tip_5_description),
                R.drawable.ic_exercise
            )
        )
    }

    private fun shareTip(tip: Tip) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, tip.title)
            putExtra(Intent.EXTRA_TEXT, "${tip.title}\n\n${tip.description}")
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_tip)))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
