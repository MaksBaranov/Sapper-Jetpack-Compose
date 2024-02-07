package com.task.feature_sapper

import com.task.feature_sapper.entity.Difficulty
import com.task.feature_sapper.entity.SapperField
import javax.inject.Inject

class SapperInteractor @Inject constructor(
    private val getSapperParamsUseCase: GetSapperParamsUseCase,
    private val getGeneratingGridUseCase: GetGeneratingGridUseCase,
    private val getGeneratingMinesUseCase: GetGeneratingMinesUseCase,
    private val createSapperGameUseCase: CreateSapperGameUseCase,
    private val getUpdatingSapperFieldUseCase: GetUpdatingSapperFieldUseCase,
    private val updateSapperFlagUseCase: UpdateSapperFlagUseCase
) {

    suspend fun initGame(difficulty: Difficulty): SapperField {
        val sapperParams = getSapperParamsUseCase.execute(difficulty)
        val grid = getGeneratingGridUseCase.execute(sapperParams)
        val minesPositions = getGeneratingMinesUseCase.execute(sapperParams)

        return createSapperGameUseCase.execute(
            CreateSapperGameUseCase.Params(
                difficulty, grid, minesPositions
            )
        )
    }

    suspend fun openCell(row: Int, col: Int, sapperField: SapperField): SapperField {
        return getUpdatingSapperFieldUseCase.execute(
            GetUpdatingSapperFieldUseCase.Params(
                row = row,
                col = col,
                sapperField = sapperField

            )
        )
    }

    suspend fun onUpdateFlag(row: Int, col: Int, sapperField: SapperField): SapperField {
        return updateSapperFlagUseCase.execute(
            UpdateSapperFlagUseCase.Params(
                row,
                col,
                sapperField
            )
        )
    }
}