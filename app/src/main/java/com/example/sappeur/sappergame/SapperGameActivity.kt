package com.example.sappeur.sappergame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Square
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.sappeur.R
import com.example.sappeur.ui.theme.BIG
import com.example.sappeur.ui.theme.CELL_FONT_SIZE
import com.example.sappeur.ui.theme.CELL_PADDING
import com.example.sappeur.ui.theme.CELL_SIZE
import com.example.sappeur.ui.theme.SapperTheme
import com.task.feature_sapper.entity.CellState
import com.task.feature_sapper.entity.Difficulty
import com.task.feature_sapper.entity.GameStatus
import com.task.feature_sapper.entity.SapperCell
import com.task.feature_sapper.entity.SapperField
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
                    SapperScreen()
                }
            }
        }
    }


    @Composable
    fun SapperScreen() {
        val gameState by viewModel.gameFieldState.collectAsState()
        var selectedDifficulty = Difficulty.SMALL

        if (gameState.gameStatus == GameStatus.OVER) {

            AlertDialog(
                onDismissRequest = { },
                title = { Text(text = getString(R.string.sapper_game_over)) },
                text = { Text(text = getString(R.string.sapper_hite_mine)) },
                confirmButton = {
                    Button(onClick = {
                        viewModel.initGame(Difficulty.SMALL)
                    }) {
                        Text(text = getString(R.string.sapper_new_game))
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {


            ConstraintLayout(modifier = Modifier.fillMaxWidth(1f)) {

                val (box, icon) = createRefs()
                Box(
                    modifier = Modifier.constrainAs(box) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }

                ) {
                    DifficultySelector(selectedDifficulty) { newDifficulty ->
                        selectedDifficulty = newDifficulty
                        viewModel.initGame(newDifficulty)
                    }
                }


                Icon(
                    imageVector = Icons.Default.Autorenew,
                    contentDescription = null,
                    tint = Color.Green,
                    modifier = Modifier
                        .constrainAs(icon) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .width(BIG)
                        .height(BIG)
                        .clickable { viewModel.initGame(selectedDifficulty) }

                )
            }


            MinesweeperGrid(
                game = gameState,
                onCellClicked = { row, col ->
                    viewModel.openCell(row, col)
                },
                onCellLongClicked = { row, col -> viewModel.onUpdateFlag(row, col) }
            )

            Spacer(modifier = Modifier.height(16.dp))

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DifficultySelector(
        selectedDifficulty: Difficulty,
        onDifficultySelected: (Difficulty) -> Unit
    ) {
        val difficultyOptions = listOf(Difficulty.SMALL, Difficulty.MEDIUM, Difficulty.LARGE)
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionIndex by remember { mutableStateOf(0) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {

            BasicTextField(
                value = difficultyOptions[selectedOptionIndex].name,
                onValueChange = { },
                readOnly = true,
                singleLine = true,
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(expanded = expanded,
                onDismissRequest = { expanded = false }) {
                difficultyOptions.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        text = { Text(text = option.name) },
                        onClick = {
                            selectedOptionIndex = index
                            onDifficultySelected(option)
                            expanded = false
                        })
                }
            }

        }
    }


    @Composable
    fun ActionContextMenu(
        onFlagClicked: () -> Unit,
        onDigClicked: () -> Unit,
        onCloseClicked: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .background(Color.Transparent)
        ) {

            Row {
                Icon(
                    imageVector = Icons.Default.Flag,
                    contentDescription = null,
                    tint = Color.Blue,
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            onFlagClicked()
                        }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_shovel),
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier

                        .padding(4.dp)
                        .clickable {
                            onDigClicked()
                        }
                )


            }
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .padding(4.dp)
                    .clickable {
                        onDigClicked()
                    }
            )

        }
    }


    @Composable
    fun MinesweeperGrid(
        game: SapperField,
        onCellClicked: (Int, Int) -> Unit,
        onCellLongClicked: (Int, Int) -> Unit
    ) {
        LazyColumn {
            items(game.rows) { rowIndex ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    game.grid[rowIndex].forEachIndexed { colIndex, cell ->
                        SapperCell(
                            cell = cell,
                            onCellClicked = {
                                onCellClicked(
                                    rowIndex,
                                    colIndex
                                )
                            },
                            onCellLongClicked = {
                                onCellLongClicked(
                                    rowIndex,
                                    colIndex
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun SapperCell(
        cell: SapperCell,
        onCellClicked: () -> Unit,
        onCellLongClicked: () -> Unit
    ) {
        val shape = RoundedCornerShape(corner = CornerSize(8.dp))
        var isContextMenuOpen by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .size(40.dp)
                .combinedClickable(onClick = {
                    if (cell.state == CellState.Hidden)
                        isContextMenuOpen = true

                },
                    onLongClick = { onCellLongClicked() })
                .background(MaterialTheme.colorScheme.background, shape)
        ) {
            if (isContextMenuOpen) {
                Popup(
                    onDismissRequest = {
                        isContextMenuOpen = false
                    },
                    alignment = Alignment.TopStart
                ) {
                    ActionContextMenu(
                        onFlagClicked = {
                            viewModel.onUpdateFlag(cell.row, cell.col)
                            isContextMenuOpen = false
                        },
                        onDigClicked = {

                            viewModel.openCell(cell.row, cell.col)
                            isContextMenuOpen = false
                        },
                        onCloseClicked = {
                            isContextMenuOpen = false
                        }
                    )


                }

            }

            when (cell.state) {
                CellState.Hidden -> Icon(
                    imageVector = Icons.Default.Square,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp)
                )

                CellState.Revealed -> {
                    if (cell.adjacentMines > 0) {

                        Box(
                            modifier = Modifier
                                .size(CELL_SIZE)
                                .background(Color.Yellow)
                                .align(Alignment.Center),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cell.adjacentMines.toString(),
                                fontSize = CELL_FONT_SIZE,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(CELL_PADDING)
                                    .align(Alignment.Center)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.CropSquare,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }

                }

                CellState.Flagged ->
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier
                            .fillMaxSize()
                    )

                CellState.Mined ->
                    Icon(
                        imageVector = Icons.Default.FiberManualRecord,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier
                            .fillMaxSize()
                    )
            }

        }
    }
}

