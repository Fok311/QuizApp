package com.fok.quizappproject.core.di

import com.fok.quizappproject.core.service.AuthService
import com.fok.quizappproject.data.repo.QuizQuestionRepo
import com.fok.quizappproject.data.repo.QuizQuestionRepoImpl
import com.fok.quizappproject.data.repo.QuizRepo
import com.fok.quizappproject.data.repo.QuizRepoImpl
import com.fok.quizappproject.data.repo.ResultRepo
import com.fok.quizappproject.data.repo.ResultRepoImpl
import com.fok.quizappproject.data.repo.UserRepo
import com.fok.quizappproject.data.repo.UserRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {
    @Provides
    @Singleton
    fun providesUserRepo(authService: AuthService): UserRepo {
        return UserRepoImpl(authService = authService)
    }

    @Provides
    @Singleton
    fun providesQuizRepo(authService: AuthService): QuizRepo {
        return QuizRepoImpl(authService = authService)
    }

    @Provides
    @Singleton
    fun providesQuizQuestionRepo(authService: AuthService): QuizQuestionRepo {
        return QuizQuestionRepoImpl(authService = authService)
    }

    @Provides
    @Singleton
    fun providesResultRepo(authService: AuthService): ResultRepo {
        return ResultRepoImpl(authService = authService)
    }
}