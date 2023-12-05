package com.fok.quizappproject.ui.Quiz

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.fok.quizappproject.R
import com.fok.quizappproject.data.Model.Quiz
import com.fok.quizappproject.databinding.FragmentQuizBinding
import com.fok.quizappproject.ui.Quiz.viewModel.QuizViewModelImpl
import com.fok.quizappproject.ui.adapter.QuestionAdapter
import com.fok.quizappproject.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizFragment : BaseFragment<FragmentQuizBinding>() {
    private val args: QuizFragmentArgs by navArgs()
    override val viewModel: QuizViewModelImpl by viewModels()
    private var titleNum = 0
    private var optionNum = 0
    private var optionNum1 = 1
    private var optionNum2 = 2
    private var optionNum3 = 3
    private var answerNum = 0
    private var result = 0
    private var choose = ""
    private var answer = ""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        viewModel.getQuiz(args.quizId)


        binding.btnFinish.setOnClickListener {
            val action = QuizFragmentDirections.toHome()
            navController.navigate(action)
        }

        binding.btnSubmit.setOnClickListener {
            when (binding.rgQuiz.checkedRadioButtonId) {
                R.id.answerA -> choose = binding.answerA.text.toString()
                R.id.answerB -> choose = binding.answerB.text.toString()
                R.id.answerC -> choose = binding.answerC.text.toString()
                R.id.answerD -> choose = binding.answerD.text.toString()
            }

            titleNum += 1
            optionNum += 4
            optionNum1 += 4
            optionNum2 += 4
            optionNum3 += 4
            answerNum += 1

            if(choose == answer) {
                result += 1
            }


            lifecycleScope.launch {
                viewModel.quiz.collect {
                    val greetingText = "Your Score: "
                    val fullName = "$greetingText${result}/${it.titles.size}"
                    binding.tvScore.text = fullName
                }
            }

            lifecycleScope.launch {
                viewModel.quiz.collect {
                    if(titleNum > 0){
                        if(titleNum == it.titles.size){
                            binding.LLQuestion.visibility = View.GONE
                            binding.LLResult.visibility = View.VISIBLE
                            viewModel.addResult(result.toString(), it.QuizId)
                        }
                    }
                    updateQuiz(it)
                }
            }
        }
    }

    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)


        lifecycleScope.launch {
            viewModel.quiz.collect {
                updateQuiz(it)
                Log.d("debugging", "awsdsacmi ${it}")
                if( it.time.toInt() != null || it.time.toInt() != 0) {
                    val countdownTimer = object : CountDownTimer(it.time * 60 * 1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            // Update the countdown TextView with the remaining time
                            val secondsRemaining = millisUntilFinished / 1000
                            Log.d("debugging", secondsRemaining.toString())
                            binding.tvTimer.text = "Time Remaining: $secondsRemaining"
                            val time = secondsRemaining
                            if(time.toInt() == 0){
//                                binding.LLQuestion.visibility = View.GONE
//                                binding.LLResult.visibility = View.VISIBLE
//                                viewModel.addResult(result.toString(), it.QuizId)
                            }
                        }

                        override fun onFinish() {
                            // Handle what to do when the countdown finishes
                            binding.LLQuestion.visibility = View.GONE
                            binding.LLResult.visibility = View.VISIBLE
                            viewModel.addResult(result.toString(), it.QuizId)
                        }
                    }
                    countdownTimer.start()
                }
            }
        }
    }

    private fun updateQuiz(quiz: Quiz){
        binding.run {
            val questionTitle = quiz.titles.getOrNull(titleNum) ?: ""
            tvQuestion.text = questionTitle

            // Assuming that options list has at least 4 items
            answerA.text = quiz.options.getOrNull(optionNum) ?: ""
            answerB.text = quiz.options.getOrNull(optionNum1) ?: ""
            answerC.text = quiz.options.getOrNull(optionNum2) ?: ""
            answerD.text = quiz.options.getOrNull(optionNum3) ?: ""

            answer = quiz.answers.getOrNull(answerNum) ?: ""
        }
    }

}