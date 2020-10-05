package com.interview.arctouchinterview.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.interview.arctouchinterview.R
import com.interview.arctouchinterview.model.Keyword
import com.interview.arctouchinterview.viewmodel.QuizAppViewModel
import kotlinx.android.synthetic.main.activity_main.*

class QuizApp : AppCompatActivity() {

    private val adapter = RightWordsAdapter(emptyList<Keyword>().toMutableList())
    private val viewModel: QuizAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.rightScore.observe(this, {
            score.text = "$it/${viewModel.allWords.value?.size ?: 0}"
            if(it == viewModel.allWords.value?.size ?: 0) {
                viewModel.resetTimer()
                AlertDialog
                    .Builder(this)
                    .setTitle("You get all, well done!!")
                    .setPositiveButton("ok", null)
                    .show()
            }
        })

        viewModel.rightWords.observe(this, {
            adapter.updateList(it)
        })

        viewModel.actualTime.observe(this, {
            val seconds = it / 1000
            val minutes = seconds / 60
            val secondsText = (seconds % 60).toString().padStart(2, '0')
            val minutesText = minutes.toString().padStart(2,'0')
            timer.text = "$minutesText:$secondsText"
        })

        submit_btn.setOnClickListener {
            viewModel.startTimer()
            val text = reserved_input.text
            val rightWord = viewModel.checkRightWord(text.toString())
            Snackbar.make(root, if (rightWord) "Right!" else "Wrong...", Snackbar.LENGTH_SHORT).show()
            reserved_input.setText("")
        }

        right_words.adapter = adapter
    }

}