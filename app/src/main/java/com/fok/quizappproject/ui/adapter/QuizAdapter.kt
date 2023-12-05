package com.fok.quizappproject.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fok.quizappproject.data.Model.Quiz
import com.fok.quizappproject.data.Model.Result
import com.fok.quizappproject.databinding.ItemLayoutQuizBinding

class QuizAdapter(
    private var quizs :List<Quiz>,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(child: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemLayoutQuizBinding.inflate(
            LayoutInflater.from(child.context),
            child,
            false
        )
        return QuizViewHolder(binding)
    }

    override fun getItemCount() = quizs.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val quiz = quizs[position]
        if (holder is QuizViewHolder) {
            holder.bind(quiz)
        }
    }

    fun setQuiz(quizs: List<Quiz>) {
        this.quizs = quizs
        notifyDataSetChanged()
    }

    inner class QuizViewHolder(
        private val binding: ItemLayoutQuizBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: Quiz) {
            binding.run {
                tvQuizId.text = "id: ${quiz.QuizId}"
                tvTitle.text = "title: ${quiz.title}"
                tvCsv.text = "created by: ${quiz.createdWith}"
            }
        }
    }
}