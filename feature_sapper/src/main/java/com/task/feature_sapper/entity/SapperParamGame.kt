package com.task.feature_sapper.entity

sealed class SapperParamGame(
    val rows: Int,
    val cols: Int,
    val minesCount: Int
)

class SmallSapperParamGame(
) : SapperParamGame(
    rows = 9,
    cols = 9,
    minesCount = 10
)


class MediumSapperParamGame(
) : SapperParamGame(
    rows = 16,
    cols = 16,
    minesCount = 40
)

class LargeSapperParamGame(
) : SapperParamGame(
    rows = 30,
    cols = 16,
    minesCount = 99
)

