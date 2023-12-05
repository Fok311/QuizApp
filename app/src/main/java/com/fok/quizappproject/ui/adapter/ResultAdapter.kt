package com.fok.quizappproject.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fok.quizappproject.data.Model.Result
import com.fok.quizappproject.databinding.LayoutLeaderboardBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultAdapter(
  private var result: List<Result>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(child: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutLeaderboardBinding.inflate(
            LayoutInflater.from(child.context),
            child,
            false
        )
        return ResultViewHolder(binding)
    }

    override fun getItemCount() = result.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = result[position]
        if (holder is ResultAdapter.ResultViewHolder) {
            holder.bind(result)
        }
    }

    fun setResult(result: List<Result>) {
        this.result = result
        notifyDataSetChanged()

    }

    inner class ResultViewHolder(
        private val binding: LayoutLeaderboardBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.run {
                tvName.text = "Name: ${result.name}"
                tvResult.text = "Result: ${result.result}"
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                val formattedDate = dateFormat.format(Date(result.createdAt))
                tvFinishtime.text = formattedDate
                tvQuizId.text = "Quiz ID: ${result.quizId}"
            }
        }
    }

}