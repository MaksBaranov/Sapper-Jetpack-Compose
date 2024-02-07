package com.task.feature_sapper

interface BaseUseCase<in Params, out Result> {
    suspend fun execute(params: Params): Result
}