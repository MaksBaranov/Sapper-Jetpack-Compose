package com.task.feature_sapper

import com.task.feature_sapper.entity.CellState
import com.task.feature_sapper.entity.SapperCell
import com.task.feature_sapper.entity.SapperField
import javax.inject.Inject

class UpdateSapperFlagUseCase @Inject constructor() :
    BaseUseCase<UpdateSapperFlagUseCase.Params, SapperField> {

    override suspend fun execute(params: Params): SapperField {

        val grid = params.field.grid
        val row = params.row
        val col = params.col
        if (grid[row][col].state == CellState.Revealed) {
            return params.field
        }

        val newGrid = grid.toMutableList().map { it.toMutableList() }

        when (grid[row][col].state) {
            CellState.Hidden -> {
                newGrid[row][col] = SapperCell(row,col,state = CellState.Flagged )
            }

            CellState.Flagged -> {
                newGrid[row][col] = SapperCell(row,col,state = CellState.Hidden,)
            }

            else -> return params.field
        }

        return params.field.copy(grid = newGrid)
    }


    data class Params(val row: Int, val col: Int, val field: SapperField)
}