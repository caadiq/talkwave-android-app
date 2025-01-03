package com.app.talkwave.model.di

import android.content.Context
import com.app.talkwave.model.data.DataStoreModule
import com.app.talkwave.model.repository.AuthRepository
import com.app.talkwave.model.repository.ChatRepository
import com.app.talkwave.model.repository.DataStoreRepository
import com.app.talkwave.model.repository.DeptRepository
import com.app.talkwave.model.service.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideDataStoreModule(@ApplicationContext context: Context) = DataStoreModule(context)

    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStoreModule: DataStoreModule) = DataStoreRepository(dataStoreModule)

    @Provides
    @Singleton
    fun provideChatRepository(retrofit: Retrofit): ChatRepository = ChatRepository(retrofit)

    @Provides
    @Singleton
    fun provideDeptRepository(retrofit: Retrofit): DeptRepository = DeptRepository(retrofit)

    @Provides
    @Singleton
    fun provideAuthRepository(retrofit: Retrofit): AuthRepository = AuthRepository(retrofit)
}