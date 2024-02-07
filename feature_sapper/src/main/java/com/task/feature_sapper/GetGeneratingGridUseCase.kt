package com.task.feature_sapper

import com.task.feature_sapper.entity.SapperCell
import com.task.feature_sapper.entity.SapperParamGame
import javax.inject.Inject

class GetGeneratingGridUseCase @Inject constructor() :
    BaseUseCase<SapperParamGame, List<List<SapperCell>>> {

    override suspend fun execute(params: SapperParamGame): List<List<SapperCell>> {
        return generateGrid(params.rows, params.cols)
    }

    private fun generateGrid(rows: Int, cols: Int): List<List<SapperCell>> = List(rows) { row ->
        List(cols) { col ->
            SapperCell(row, col)
        }
    }
}