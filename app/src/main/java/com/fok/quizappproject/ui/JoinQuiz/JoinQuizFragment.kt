package com.fok.quizappproject.ui.JoinQuiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fok.quizappproject.databinding.FragmentJoinQuizBinding
import com.fok.quizappproject.ui.JoinQuiz.viewModel.JoinQuizViewModelImpl
import com.fok.quizappproject.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JoinQuizFragment : BaseFragment<FragmentJoinQuizBinding>() {

    override val viewModel: JoinQuizViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentJoinQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)

        binding.run {
            btnStart.setOnClickListener {
                    val id =  etQuizId.text.toString()

                    viewModel.getQuiz(id)
                    val action = JoinQuizFragmentDirections.toQuiz(id)
                    navController.navigate(action)
            }
        }
    }

    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)

        lifecycleScope.launch {
            viewModel.quiz.collect {
                Log.d("debugging", it.toString())
            }
        }
    }

    private fun showErrorMessage(message: String) {
        Log.d("debugging", "Error: $message")
    }
}