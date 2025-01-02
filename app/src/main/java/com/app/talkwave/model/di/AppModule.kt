package com.app.talkwave.model.di

import com.app.talkwave.model.repository.ChatRepository
import com.app.talkwave.model.repository.DeptRepository
import com.app.talkwave.model.service.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = RetrofitService.getRetrofit()

    @Provides
    @Singleton
    fun provideChatRepository(retrofit: Retrofit): ChatRepository = ChatRepository(retrofit)

    @Provides
    @Singleton
    fun provideDeptRepository(retrofit: Retrofit): DeptRepository = DeptRepository(retrofit)
}