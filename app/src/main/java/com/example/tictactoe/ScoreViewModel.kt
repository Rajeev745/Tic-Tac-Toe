package com.example.tictactoe

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel : ViewModel() {

    private var _scorePlayerX = MutableLiveData<Int>()
    private var _scorePlayerO = MutableLiveData<Int>()

    fun getPlayerXScore(): MutableLiveData<Int> {
        return _scorePlayerX
    }

    fun getPlayerOScore(): MutableLiveData<Int> {
        return _scorePlayerO
    }

    fun setPlayerXScore(score: Int) {
        _scorePlayerX.value = score
    }

    fun setPlayerOScore(score: Int) {
        _scorePlayerO.value = score
    }
}