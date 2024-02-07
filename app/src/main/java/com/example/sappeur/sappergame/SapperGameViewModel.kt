package com.example.sappeur.sappergame


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.feature_sapper.SapperInteractor
import com.task.feature_sapper.entity.Difficulty
import com.task.feature_sapper.entity.SapperField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SapperGameViewModel @Inject constructor(
    private val sapperInteractor: SapperInteractor
) : ViewModel() {


    private val gameState: MutableStateFlow<SapperField> = MutableStateFlow(
        SapperField(
            0,
            0,
            0,
            emptyList()
        )
    )
    val gameFieldState: StateFlow<SapperField> = gameState

    fun openCell(row: Int, col: Int) {
        viewModelScope.launch {
            gameState.value =
                sapperInteractor.openCell(row, col, gameState.value)
        }
    }

    fun onUpdateFlag(row: Int, col: Int) {
        viewModelScope.launch {
            gameState.value = sapperInteractor.onUpdateFlag(row, col, gameState.value)
        }
    }

    fun initGame(difficulty: Difficulty) {
        viewModelScope.launch {
            gameState.value =
                sapperInteractor.initGame(difficulty)
        }
    }
}