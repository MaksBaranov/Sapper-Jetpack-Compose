package com.task.feature_sapper

import com.task.feature_sapper.entity.Difficulty
import com.task.feature_sapper.entity.LargeSapperParamGame
import com.task.feature_sapper.entity.MediumSapperParamGame
import com.task.feature_sapper.entity.SapperParamGame
import com.task.feature_sapper.entity.SmallSapperParamGame
import javax.inject.Inject

class GetSapperParamsUseCase @Inject constructor() :
    BaseUseCase<Difficulty, SapperParamGame> {

    override suspend fun execute(params: Difficulty): SapperParamGame {
        return when (params) {
            Difficulty.SMALL -> SmallSapperParamGame()
            Difficulty.MEDIUM -> MediumSapperParamGame()
            Difficulty.LARGE -> LargeSapperParamGame()
        }
    }
}