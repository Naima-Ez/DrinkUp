package com.example.drinkup.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.drinkup.R
import com.example.drinkup.database.entities.GoalHistory

class GoalHistoryAdapter : ListAdapter<GoalHistory, GoalHistoryAdapter.GoalViewHolder>(
    GoalViewHolder.GoalDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_goal_history, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = getItem(position)
        holder.bind(goal)
    }

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        private val tvGoal: TextView = itemView.findViewById(R.id.tv_goal)
        private val tvConsumed: TextView = itemView.findViewById(R.id.tv_consumed)
        private val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        private val ivStatus: ImageView = itemView.findViewById(R.id.iv_status)

        fun bind(goal: GoalHistory) {
            tvDate.text = goal.startDate // أو goal.endDate إذا بغيت
            tvGoal.text = "${goal.objectif} ml"
            // إذا بغيت تحسب consomme (مثلاً من DrinkEntry) خصك تربطهم فالViewModel

            // الحالة
            tvStatus.text = if (goal.isActive) "🔵" else "⚪"
            ivStatus.setImageResource(
                if (goal.isActive) R.drawable.ic_check_circle else R.drawable.ic_cancel
            )
        }

        class GoalDiffCallback : DiffUtil.ItemCallback<GoalHistory>() {
        override fun areItemsTheSame(oldItem: GoalHistory, newItem: GoalHistory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GoalHistory, newItem: GoalHistory): Boolean {
            return oldItem == newItem
        }
    }
}
}
