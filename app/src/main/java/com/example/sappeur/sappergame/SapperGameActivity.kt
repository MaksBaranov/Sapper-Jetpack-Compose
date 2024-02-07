package com.example.sappeur.sappergame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.sappeur.ui.theme.SapperTheme
import com.task.feature_sapper.entity.Difficulty
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SapperGameActivity : ComponentActivity() {

    private val viewModel: SapperGameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initGame(Difficulty.SMALL)
        setContent {
            SapperTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SapperGameScreen(viewModel)
                }
            }
        }
    }
}
