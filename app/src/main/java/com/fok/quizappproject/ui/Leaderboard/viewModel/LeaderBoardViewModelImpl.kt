package com.fok.quizappproject.ui.Leaderboard.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fok.quizappproject.core.service.AuthService
import com.fok.quizappproject.data.Model.Quiz
import com.fok.quizappproject.data.Model.Result
import com.fok.quizappproject.data.repo.ResultRepo
import com.fok.quizappproject.data.repo.UserRepo
import com.fok.quizappproject.ui.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderBoardViewModelImpl @Inject constructor(
    private val resultRepo: ResultRepo,
    private val userRepo: UserRepo
) : BaseViewModel(), LeaderboardViewModel {

    private val _result: MutableStateFlow<List<Result>> = MutableStateFlow(emptyList())
    val result: StateFlow<List<Result>> = _result


    init {
        getResult()
    }

    fun getResult() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                resultRepo.getResult()
            }?.collect {
                _result.value = it
            }
        }
    }
}