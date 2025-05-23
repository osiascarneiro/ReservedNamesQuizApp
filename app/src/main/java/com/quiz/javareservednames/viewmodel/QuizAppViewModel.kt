package com.quiz.javareservednames.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quiz.javareservednames.model.Keyword
import com.quiz.javareservednames.model.KeywordsMemoryRepository
import com.quiz.javareservednames.model.KeywordsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class QuizAppViewModel(
    repository: KeywordsRepository = KeywordsMemoryRepository()
): ViewModel() {

    val timerLimit: Long = 60000*5
    private var timer = createTimer()

    val rightScore = MutableStateFlow(0)
    val actualTimeFlow = MutableStateFlow(0L)
    val allWordsFlow = MutableStateFlow(emptyList<Keyword>())
    val rightWordsFlow = MutableStateFlow(emptyList<Keyword>())

    private var timerRunning = false

    init {
        viewModelScope.launch {
            allWordsFlow.emit(repository.getInitialWords())
            rightWordsFlow.emit(emptyList())
            rightScore.emit(0)
        }
    }

    private fun createTimer(): CountDownTimer {
        return object: CountDownTimer(timerLimit, 1000) {
            override fun onTick(second: Long) {
                viewModelScope.launch {
                    actualTimeFlow.emit(second)
                }
            }

            override fun onFinish() {}

        }
    }

    fun checkRightWord(text: String): Boolean {
        val word = allWordsFlow.value.firstOrNull { it.value.trim().uppercase() == text.trim().uppercase() }
        word?.let {
            val list = rightWordsFlow.value.toMutableList()
            if(!list.contains(it)) {
                list.add(it)
                rightWordsFlow.value = list
                rightScore.value = rightScore.value.plus(1)
            }
        }
        return word != null
    }

    fun startTimer() {
        if(timerRunning) return
        timer.start()
        timerRunning = true
    }

    fun resetTimer() {
        timerRunning = false
        timer.cancel()
        timer = createTimer()
    }

}