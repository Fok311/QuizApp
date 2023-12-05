package com.fok.quizappproject.ui.Leaderboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fok.quizappproject.R
import com.fok.quizappproject.databinding.FragmentLeaderBoardBinding
import com.fok.quizappproject.ui.Leaderboard.viewModel.LeaderBoardViewModelImpl
import com.fok.quizappproject.ui.adapter.QuizAdapter
import com.fok.quizappproject.ui.adapter.ResultAdapter
import com.fok.quizappproject.ui.base.BaseFragment
import com.fok.quizappproject.ui.home.viewModel.HomeViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LeaderBoardFragment : BaseFragment<FragmentLeaderBoardBinding>() {

    override val viewModel: LeaderBoardViewModelImpl by viewModels()
    private lateinit var adapter: ResultAdapter
    private lateinit var categoryAdapter: ArrayAdapter<String>
    protected var categorySelect = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLeaderBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUiComponents(view: View) {
        super.setupUiComponents(view)
        setupAdapter()

        categoryAdapter = ArrayAdapter(
            requireContext(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            emptyList()
        )

        binding.run {
            autoCompleteCategory.addTextChangedListener {
                categorySelect = it.toString()
                Log.d("debugging", categorySelect)
            }
        }
    }

    override fun setupViewModelObserver(view: View) {
        super.setupViewModelObserver(view)

        lifecycleScope.launch {
            viewModel.result.collect {results ->
                val quizIds = results.map { it.quizId }
                Log.d("debugging", quizIds.toString())
                categoryAdapter = ArrayAdapter(
                    requireContext(),
                    androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                    quizIds
                )
                binding.autoCompleteCategory.setAdapter(categoryAdapter)
                // Update the ResultAdapter with the full list of results
                adapter.setResult(results)
            }
        }
    }

    private fun setupAdapter() {
        adapter = ResultAdapter(emptyList())
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvLeaderBoard.adapter = adapter
        binding.rvLeaderBoard.layoutManager = layoutManager
    }
}