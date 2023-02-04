package com.example.tictactoe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

interface OnDataPass {
    fun onDataPass(data: String)
}

class TicTacToeFragment : Fragment(), OnDataPass {

    lateinit var scoreViewModel: ScoreViewModel
    lateinit var ticTacToeView: TicTacToeView
    private lateinit var rootView: View
    private lateinit var resetButton: Button
    private lateinit var winnerText: TextView
    private lateinit var playerXScoreText: TextView
    private lateinit var playerOScoreText: TextView
    private var scorePlayerX: Int = 0
    private var scorePlayerO: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_tic_tac_toe, container, false)
        return rootView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerOScoreText = rootView.findViewById(R.id.player_O_score)
        playerXScoreText = rootView.findViewById(R.id.player_X_score)
        winnerText = rootView.findViewById(R.id.Winner_Text)
        resetButton = rootView.findViewById(R.id.reset_game)
        ticTacToeView = rootView.findViewById(R.id.tic_tac_toe)
        scoreViewModel = ViewModelProvider(this).get(ScoreViewModel::class.java)
        ticTacToeView.setOnDataPassListener(this)

        resetButton.setOnClickListener {
            ticTacToeView.reset()
            winnerText.visibility = View.GONE
            winnerText.text = ""
        }

        scoreViewModel.getPlayerXScore().observe(viewLifecycleOwner, Observer {
            playerXScoreText.text = "Player X score: $it"
        })

        scoreViewModel.getPlayerOScore().observe(viewLifecycleOwner, Observer {
            playerOScoreText.text = "Player O score: $it"
        })
    }

    @SuppressLint("SetTextI18n")
    override fun onDataPass(data: String) {

        Log.d("winner", data)

        winnerText.visibility = View.VISIBLE
        if (data == "X" || data == "O") winnerText.text =
            "Winner is Player: $data" else winnerText.text = "This game is $data"

        resetButton.isEnabled = true
        ticTacToeView.isEnabled = false

        if (data == "X") {
            scoreViewModel.setPlayerXScore(++scorePlayerX/2)
        } else if (data == "O") {
            scoreViewModel.setPlayerOScore(++scorePlayerO/2)
        }
    }

}
