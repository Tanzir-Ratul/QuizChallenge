package com.example.quizchallenge.dependencyInjection

import android.app.Application
import android.content.Context
import com.example.quizchallenge.ui.repository.QuizRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRepository(@ApplicationContext ctx: Context): QuizRepository {
        return QuizRepository(ctx)
    }
}