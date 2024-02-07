package com.task.feature_sapper

import com.task.feature_sapper.entity.Difficulty
import com.task.feature_sapper.entity.LargeSapperParamGame
import com.task.feature_sapper.entity.MediumSapperParamGame
import com.task.feature_sapper.entity.SapperCell
import com.task.feature_sapper.entity.SapperField
import com.task.feature_sapper.entity.SmallSapperParamGame
import javax.inject.Inject

class CreateSapperGameUseCase @Inject constructor() :
    BaseUseCase<CreateSapperGameUseCase.Params, SapperField> {

    override suspend fun execute(params: Params): SapperField {

        val sapperGameParams = when (params.difficulty) {
            Difficulty.SMALL -> SmallSapperParamGame()
            Difficulty.MEDIUM -> MediumSapperParamGame()
            Difficulty.LARGE -> LargeSapperParamGame()
        }

        val rows = sapperGameParams.rows
        val cols = sapperGameParams.cols
        val minesCount = sapperGameParams.minesCount

        val listWithMines = placeMines(params.grid, params.minesPositions)
        val resultList = calculateAdjacentMines(listWithMines, rows, cols)

        return SapperField(
            rows = rows,
            cols = cols,
            minesCount = minesCount,
            grid = resultList
        )
    }

    private fun placeMines(
        currentGrid: List<List<SapperCell>>,
        minesPositions: List<Pair<Int, Int>>
    ): List<List<SapperCell>> {

        val gridWithMines = currentGrid.toMutableList().map { it.toMutableList() }
        for ((row, col) in minesPositions) {
            gridWithMines[row][col] = SapperCell(row, col, isMine = true)
        }
        return gridWithMines
    }

    private fun calculateAdjacentMines(
        grid: List<List<SapperCell>>,
        rows: Int,
        cols: Int
    ): List<List<SapperCell>> {
        val currentGrid = grid.toMutableList().map { it.toMutableList() }
        for (row in 0 until rows - 1) {
            for (col in 0 until cols - 1) {
                if (!grid[row][col].isMine) {
                    val adjacentMinesCount = getNearestCells(row, col, rows, cols)
                        .count { (adjRow, adjCol) -> grid[adjRow][adjCol].isMine }

                    currentGrid[row][col] =
                        grid[row][col].copy(adjacentMines = adjacentMinesCount)
                }
            }
        }
        return currentGrid
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

    data class Params(
        val difficulty: Difficulty,
        val grid: List<List<SapperCell>>,
        val minesPositions: List<Pair<Int, Int>>
    )
}


