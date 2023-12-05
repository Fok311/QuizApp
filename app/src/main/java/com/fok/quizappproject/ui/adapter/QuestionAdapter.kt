package com.fok.quizappproject.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fok.quizappproject.data.Model.Quiz
import com.fok.quizappproject.databinding.QuestionLayoutQuizBinding

class QuestionAdapter(
    private var quizs: List<Quiz>,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(child: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = QuestionLayoutQuizBinding.inflate(
            LayoutInflater.from(child.context),
            child,
            false
        )
        return QuizViewHolder(binding)
    }

    override fun getItemCount() = quizs.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val quiz = quizs[position]
        if (holder is QuizAdapter.QuizViewHolder) {
            holder.bind(quiz)
        }
    }

    fun setQuiz(quizs: List<Quiz>) {
        this.quizs = quizs
        notifyDataSetChanged()
    }

    inner class QuizViewHolder(
        private val binding: QuestionLayoutQuizBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: Quiz) {
            binding.run {
                // Assuming that titles and options lists are non-empty
                val questionTitle = quiz.titles.firstOrNull() ?: ""
                tvQuestion.text = questionTitle

                // Assuming that options list has at least 4 items
                answerA.text = quiz.options.getOrNull(0) ?: ""
                answerB.text = quiz.options.getOrNull(1) ?: ""
                answerC.text = quiz.options.getOrNull(2) ?: ""
                answerD.text = quiz.options.getOrNull(3) ?: ""
            }
        }
    }
}