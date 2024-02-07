package com.task.feature_sapper

import com.task.feature_sapper.entity.CellState
import com.task.feature_sapper.entity.GameStatus
import com.task.feature_sapper.entity.SapperCell
import com.task.feature_sapper.entity.SapperField
import javax.inject.Inject

class GetUpdatingSapperFieldUseCase @Inject constructor() :
    BaseUseCase<GetUpdatingSapperFieldUseCase.Params, SapperField> {

    override suspend fun execute(params: Params): SapperField {

        val grid = params.sapperField.grid
        val row = params.row
        val col = params.col

        val cellState = grid[row][col].state
        if (cellState == CellState.Revealed) {
            return params.sapperField
        }
        val newGrid = grid.toMutableList().map { it.toMutableList() }
        return when (cellState) {
            CellState.Hidden ->
                if (grid[row][col].isMine) {
                    params.sapperField.copy(
                        gameStatus = GameStatus.OVER,
                        grid = revealAllMines(newGrid)
                    )
                } else {
                    newGrid[row][col] =
                        grid[row][col].copy(
                            state = CellState.Revealed,
                            adjacentMines = grid[row][col].adjacentMines
                        )
                    if (grid[row][col].adjacentMines == 0) {
                        params.sapperField.copy(grid = openEmptyCells(newGrid, row, col))

                    } else {
                        params.sapperField.copy(grid = newGrid)
                    }
                }

            else -> params.sapperField
        }
    }

    private fun revealAllMines(grid: List<List<SapperCell>>): List<List<SapperCell>> {
        return grid.map { list ->
            list.map { cell ->
                if (cell.isMine) {
                    return@map cell.copy(state = CellState.Mined)
                }
                return@map cell
            }
        }
    }


    private fun openEmptyCells(
        grid: List<List<SapperCell>>,
        row: Int,
        col: Int
    ): List<List<SapperCell>> {
        val visited = mutableSetOf<Pair<Int, Int>>()
        val toVisit = mutableListOf<Pair<Int, Int>>()

        toVisit.add(Pair(row, col))
        visited.add(Pair(row, col))
        val newGrid = grid.toMutableList().map { it.toMutableList() }
        while (toVisit.isNotEmpty()) {
            val (currentRow, currentCol) = toVisit.removeAt(0)

            for ((adjRow, adjCol) in getNearestCells(
                currentRow,
                currentCol,
                grid.size,
                grid.first().size
            )) {
                if (!visited.contains(Pair(adjRow, adjCol))) {
                    visited.add(Pair(adjRow, adjCol))
                    if (grid[adjRow][adjCol].adjacentMines == 0) {
                        toVisit.add(Pair(adjRow, adjCol))
                    }
                    newGrid[adjRow][adjCol] = grid[adjRow][adjCol].copy(state = CellState.Revealed)
                }
            }
        }

        return newGrid
    }

    private fun getNearestCells(row: Int, col: Int, rows: Int, cols: Int): List<Pair<Int, Int>> =
        listOf(
            Pair(row - 1, col - 1),
            Pair(row - 1, col),
            Pair(row - 1, col + 1),
            Pair(row, col - 1),
            Pair(row, col + 1),
            Pair(row + 1, col - 1),
            Pair(row + 1, col),
            Pair(row + 1, col + 1)
        ).filter { (adjRow, adjCol) ->
            adjRow in 0..<rows && adjCol in 0..<cols
        }

    data class Params(val row: Int, val col: Int, val sapperField: SapperField)
}