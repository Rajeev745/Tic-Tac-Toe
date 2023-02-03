package com.example.tictactoe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

interface OnDataPass {
    fun onDataPass(data: String)
}

class TicTacToeFragment : Fragment(), OnDataPass {

    lateinit var ticTacToeView: TicTacToeView
    private lateinit var resetButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val rootView = inflater.inflate(R.layout.fragment_tic_tac_toe, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resetButton = view.findViewById(R.id.reset_game)
        ticTacToeView = view.findViewById(R.id.tic_tac_toe)

        ticTacToeView.setOnDataPassListener(this)

        resetButton.setOnClickListener {
            ticTacToeView.reset()
        }
    }

    override fun onDataPass(data: String) {
        resetButton.isEnabled = true
        Log.d("winner", data)
    }
}
