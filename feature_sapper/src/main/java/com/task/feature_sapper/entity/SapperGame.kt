package com.task.feature_sapper.entity

data class SapperField(
    val rows: Int,
    val cols: Int,
    val minesCount: Int,
    val grid: List<List<SapperCell>>  ,
    var gameStatus:GameStatus = GameStatus.PLAYING
)