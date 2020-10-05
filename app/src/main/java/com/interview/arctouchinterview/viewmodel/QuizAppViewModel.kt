package com.interview.arctouchinterview.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.interview.arctouchinterview.model.Keyword
import com.interview.arctouchinterview.model.KeywordsMemmoryRepository
import com.interview.arctouchinterview.model.KeywordsRepository

class QuizAppViewModel(
    repository: KeywordsRepository = KeywordsMemmoryRepository()
): ViewModel() {

    val timerLimit: Long = 60000*5
    private var timer = createTimer()

    val allWords = MutableLiveData<MutableList<Keyword>>()
    val rightWords = MutableLiveData<MutableList<Keyword>>()
    val rightScore = MutableLiveData<Int>()
    val actualTime = MutableLiveData<Long>()
    private var timerRunning = false

    init {
        allWords.value = repository.getInitialWords().toMutableList()
        rightScore.value = 0
        rightWords.value = emptyList<Keyword>().toMutableList()
        Log.d("teste", "viewmodel initial value: $timerLimit")
        actualTime.value = timerLimit
    }

    private fun createTimer(): CountDownTimer {
        return object: CountDownTimer(timerLimit, 1000) {
            override fun onTick(second: Long) {
                actualTime.value = second
            }

            override fun onFinish() {}

        }
    }

    fun checkRightWord(text: String): Boolean {
        val word = allWords.value?.firstOrNull { it.value.trim().toUpperCase() == text.trim().toUpperCase() }
        word?.let {
            val list = rightWords.value ?: emptyList<Keyword>().toMutableList()
            if(!list.contains(it)) {
                list.add(it)
                rightWords.value = list
                rightScore.value = rightScore.value?.plus(1)
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