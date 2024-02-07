package com.task.feature_sapper.di

import com.task.feature_sapper.CreateSapperGameUseCase
import com.task.feature_sapper.GetUpdatingSapperFieldUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FeatureSapperModule {

    @Provides
    fun provideCreateSapperGameUseCase(): CreateSapperGameUseCase =
        CreateSapperGameUseCase()

    @Provides
    fun provideGetUpdatingSapperFieldUseCase(): GetUpdatingSapperFieldUseCase =
        GetUpdatingSapperFieldUseCase()
}