package com.example.drinkup.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drinkup.R
import com.example.drinkup.databinding.ItemOnboardingBinding

class OnboardingAdapter : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    private val pages = listOf(
        OnboardingPage(
            R.drawable.ic_tracking,
            R.string.onboarding_title_1,
            R.string.onboarding_desc_1
        ),
        OnboardingPage(
            R.drawable.ic_reminder,
            R.string.onboarding_title_2,
            R.string.onboarding_desc_2
        ),
        OnboardingPage(
            R.drawable.ic_analytics,
            R.string.onboarding_title_3,
            R.string.onboarding_desc_3
        )
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount() = pages.size

    class OnboardingViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(page: OnboardingPage) {
            binding.ivIllustration.setImageResource(page.imageRes)
            binding.tvTitle.setText(page.titleRes)
            binding.tvDescription.setText(page.descriptionRes)
        }
    }
}

data class OnboardingPage(
    val imageRes: Int,
    val titleRes: Int,
    val descriptionRes: Int
)