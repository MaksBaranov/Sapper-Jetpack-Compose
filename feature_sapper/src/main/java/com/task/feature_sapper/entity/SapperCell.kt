package com.task.feature_sapper.entity

data class SapperCell(
    val row: Int,
    val col: Int,
    val state: CellState = CellState.Hidden,
    val isMine: Boolean = false,
    val adjacentMines: Int = 0
)

enum class CellState {
    Hidden,
    Revealed,
    Flagged,
    Mined
}