package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    companion object {
        //These represent different important times in the game, such as game length.

        //This is when the game is over
        private const val DONE = 0L

        //This is number of milliseconds in a second
        private const val ONE_SECOND = 1000L

        //This is the total time of the game
        private const val COUNTDOWN_TIME = 7000L //for game set 60000L
    }

    private val timer: CountDownTimer

    // The current word
    private var _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    //The current timer
    private var _show_timer = MutableLiveData<String>()
    val show_timer: LiveData<String>
        get() = _show_timer

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    init {
        //Log.i("GameViewModel", "GameViewModel created!")
        _eventGameFinish.value = false
        resetList()
        nextWord()
        _score.value = 0
        //_word.value = ""
        //_show_timer = 0L

        //Creates a timer which triggers the end of the game when it finishes
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){

            override fun onTick(millisUntilFinished: Long) {
                _show_timer.value = DateUtils.formatElapsedTime(millisUntilFinished/1000)
            }

            override fun onFinish() {
                //nextWord()
                _eventGameFinish.value = true
            }
        }

        timer.start()

        //DateUtils.formatElapsedTime()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            //gameFinished()
            //_eventGameFinish.value = true
            resetList()
        }// else {
        _word.value = wordList.removeAt(0)
        //}
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
        timer.cancel()
    }
}