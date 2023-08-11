package com.example.quizchallenge.dependencyInjection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.quizchallenge.ui.repository.QuizRepository
import com.example.quizchallenge.ui.repository.SessionManager.Companion.PREF_FILE_NAME
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
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }
}