package com.example.drinkup.fragments


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drinkup.databinding.ItemTipBinding

data class Tip(
    val title: String,
    val description: String,
    val iconRes: Int
)

class TipsAdapter(
    private val tips: List<Tip>,
    private val onShareClick: (Tip) -> Unit
) : RecyclerView.Adapter<TipsAdapter.TipViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val binding = ItemTipBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        holder.bind(tips[position])
    }

    override fun getItemCount() = tips.size

    inner class TipViewHolder(private val binding: ItemTipBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tip: Tip) {
            binding.ivIcon.setImageResource(tip.iconRes)
            binding.tvTitle.text = tip.title
            binding.tvDescription.text = tip.description

            binding.btnShare.setOnClickListener {
                onShareClick(tip)
            }
        }
    }
}