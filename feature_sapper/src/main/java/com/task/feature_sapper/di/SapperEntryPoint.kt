package com.task.feature_sapper.di

import com.task.feature_sapper.CreateSapperGameUseCase
import com.task.feature_sapper.GetUpdatingSapperFieldUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface UseCaseModuleEntryPoint {
    fun provideCreateSapperGameUseCase(): CreateSapperGameUseCase
    fun provideGetUpdatingSapperFieldUseCase(): GetUpdatingSapperFieldUseCase
}