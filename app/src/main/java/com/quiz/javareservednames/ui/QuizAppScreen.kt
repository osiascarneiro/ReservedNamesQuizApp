package com.quiz.javareservednames.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quiz.javareservednames.R
import com.quiz.javareservednames.model.Keyword
import com.quiz.javareservednames.viewmodel.QuizAppViewModel
import kotlinx.coroutines.launch


@Composable
fun QuizAppScreen(
    modifier: Modifier = Modifier,
    quizViewModel: QuizAppViewModel = viewModel()
) {
    val rightWords by quizViewModel.rightWordsFlow.collectAsState()
    val allWords by quizViewModel.allWordsFlow.collectAsState()
    val actualTime by quizViewModel.actualTimeFlow.collectAsState()
    val actualScore by quizViewModel.rightScore.collectAsState()


    QuizAppContent(
        rightWords = rightWords,
        allWords = allWords,
        actualTime = actualTime,
        actualScore = actualScore,
        checkRightWord = quizViewModel::checkRightWord,
        resetTimer = quizViewModel::resetTimer,
        modifier = modifier
    )

    LaunchedEffect(Unit) {
        quizViewModel.startTimer()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizAppContent(
    rightWords: List<Keyword>,
    allWords: List<Keyword>,
    actualTime: Long,
    actualScore: Int,
    checkRightWord: (String) -> Boolean,
    resetTimer: () -> Unit,
    modifier: Modifier = Modifier,
    dialogInitialState: Boolean = false
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val textFieldState = rememberTextFieldState(initialText = "")
    val scope = rememberCoroutineScope()
    var shouldShoDialog by remember { mutableStateOf(dialogInitialState) }
    val focusManager = LocalFocusManager.current

    val rightAnswer = stringResource(R.string.right_answer)
    val wrongAnswer = stringResource(R.string.wrong_answer)

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = actualTime.let {
                        val seconds = it/1000
                        val minutes = seconds/60
                        val secondsText = (seconds % 60).toString().padStart(2, '0')
                        val minutesText = minutes.toString().padStart(2,'0')
                        return@let "$minutesText:$secondsText"
                    },
                    fontSize = 30.sp
                )
                Text(
                    text = "${actualScore}/${allWords.size}",
                    fontSize = 30.sp
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        modifier = Modifier.weight(1f),
                        state = textFieldState,
                        label = { Text(stringResource(R.string.type_an_reserved_java_name)) }
                    )
                    Image(
                        modifier = Modifier
                            .clickable {
                                val word = textFieldState.text.toString()
                                if (checkRightWord(word)) {
                                    textFieldState.clearText()
                                    scope.launch {
                                        snackbarHostState.showSnackbar(rightAnswer)
                                    }
                                } else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(wrongAnswer)
                                    }
                                }
                                if (actualScore == allWords.size) {
                                    shouldShoDialog = true
                                    resetTimer()
                                }
                                focusManager.clearFocus()
                            }
                            .size(48.dp),
                        painter = painterResource(android.R.drawable.ic_media_play),
                        contentDescription = ""
                    )
                }
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.right_words)
                )
                Column {
                    rightWords.forEach {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = it.value
                            )
                        }
                    }
                }
            }
            if (shouldShoDialog) {
                AlertDialog(
                    onDismissRequest = {
                        shouldShoDialog = false
                    },
                    title = {
                        Text(text = "You found all the words!")
                    },
                    text = {
                        Text(text = "You get all, well done!")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                shouldShoDialog = false
                            }
                        ) {
                            Text(text = "dismiss")
                        }
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun QuizAppScreenPreview() {
    QuizAppContent(
        rightWords = listOf(
            Keyword("class"),
            Keyword("private"),
        ),
        allWords = listOf(
            Keyword("class"),
            Keyword("private"),
            Keyword("public"),
            Keyword("interface"),
            Keyword("static"),
            Keyword("fun"),
            Keyword("data")
        ),
        actualTime = 300000,
        actualScore = 7,
        checkRightWord = { _ -> true },
        resetTimer = {},
        dialogInitialState = true
    )
}