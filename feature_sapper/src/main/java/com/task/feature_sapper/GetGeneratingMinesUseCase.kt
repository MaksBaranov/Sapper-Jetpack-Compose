package com.task.feature_sapper

import com.task.feature_sapper.entity.SapperParamGame
import javax.inject.Inject

class GetGeneratingMinesUseCase @Inject constructor() :
    BaseUseCase<SapperParamGame, List<Pair<Int, Int>>> {

    override suspend fun execute(params: SapperParamGame): List<Pair<Int, Int>> {
        return generateMines(minesCount = params.minesCount, params.rows, params.cols)
    }

    private fun generateMines(minesCount: Int, rows: Int, cols: Int): List<Pair<Int, Int>> {
        val allPositions = mutableListOf<Pair<Int, Int>>()
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                allPositions.add(Pair(row, col))
            }
        }
        return allPositions.shuffled().take(minesCount)
    }
}