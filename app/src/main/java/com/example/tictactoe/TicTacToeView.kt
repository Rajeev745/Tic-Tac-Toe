package com.example.tictactoe

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class TicTacToeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var listener: OnDataPass? = null
    private var cellHeight = 0f
    private var cellWidth = 0f
    private var currentPlayer = CellValue.X
    private var startingPoint = Pair(0, 0)
    private var finishingPoint = Pair(0, 0)
    private var cells = Array(9) { Cell(CellValue.EMPTY) }
    private var board = Array(3) { Array(3) { "0" } }

    fun setOnDataPassListener(listener: OnDataPass) {
        this.listener = listener
    }

    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
    }

    private val Xpaint = Paint().apply {
        color = Color.RED
        strokeWidth = 15f
        style = Paint.Style.STROKE
    }

    private val Opaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 15f
        style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            val width = width.toFloat()
            val height = height.toFloat()
            cellWidth = width / 3
            cellHeight = height / 3
            drawGrid(it)
            drawCell(it)
        }
        Log.d("cellWidth", cellWidth.toString())
        Log.d("cellHeight", cellHeight.toString())

        if (checkWin()) {
            canvas.let {
                val spx = (cellWidth / 2 + cellWidth * startingPoint.second.toFloat())
                val spy = (cellHeight / 2 + cellHeight * startingPoint.first.toFloat())
                val epx = (cellWidth / 2 + cellWidth * finishingPoint.second.toFloat())
                val epy = (cellHeight / 2 + cellHeight * finishingPoint.first.toFloat())
                it!!.drawLine(
                    spx, spy, epx, epy, Opaint
                )
            }
        }
    }

    private fun drawGrid(canvas: Canvas?) {
        for (i in 1..2) {
            canvas!!.drawLine(cellWidth * i, 0f, cellWidth * i, canvas.height.toFloat(), paint)
            canvas.drawLine(0f, cellHeight * i, canvas.width.toFloat(), cellHeight * i, paint)
        }
    }

    private fun drawCell(canvas: Canvas?) {
        for (i in 0..8) {
            val cell = cells[i]
            when (cell.value) {
                CellValue.X -> canvas?.let { drawX(it, i) }
                CellValue.O -> canvas?.let { drawO(it, i) }
                else -> {}
            }
        }
    }

    private fun drawX(canvas: Canvas, cellIndex: Int) {
        val x1 = (cellIndex % 3) * cellWidth
        val y1 = (cellIndex / 3) * cellHeight
        val x2 = (cellIndex % 3 + 1) * cellWidth
        val y2 = (cellIndex / 3 + 1) * cellHeight
        canvas.drawLine(x1 + 60f, y1 + 60f, x2 - 60f, y2 - 60f, Xpaint)
        canvas.drawLine(x2 - 60f, y1 + 60f, x1 + 60f, y2 - 60f, Xpaint)
    }

    private fun drawO(canvas: Canvas, cellIndex: Int) {
        val cx = (cellIndex % 3 + 0.5f) * cellWidth
        val cy = (cellIndex / 3 + 0.5f) * cellHeight
        canvas.drawCircle(cx, cy, cellWidth.coerceAtMost(cellHeight) / 3 - 10, Opaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    val row = (event.y / cellHeight).toInt()
                    val column = (event.x / cellHeight).toInt()
                    val index = row * 3 + column

                    Log.d("row", row.toString())
                    Log.d("column", column.toString())
                    Log.d("index", index.toString())

                    if (cells[index].value == CellValue.EMPTY) {
                        cells[index].value = currentPlayer
                        Log.d("Current player", currentPlayer.toString())
                        board[row][column] = currentPlayer.toString()

                        currentPlayer =
                            if (currentPlayer == CellValue.X) CellValue.O else CellValue.X

                        invalidate()
                        checkWin()
                        checkDraw()
                    }
                }
            }
        }
        return true
    }

    fun checkDraw() {
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == "0") {
                    return
                }
            }
        }
//        listener?.onDataPass("draw")
    }

    fun checkWin(): Boolean {
        for (i in 0..2) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != "0") {
                listener?.onDataPass(board[i][0])
                startingPoint = Pair(i, 0)
                finishingPoint = Pair(i, 2)
                return true
            }
        }

        for (i in 0..2) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != "0") {
                listener?.onDataPass(board[0][i])
                startingPoint = Pair(0, i)
                finishingPoint = Pair(2, i)
                return true
            }
        }

        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != "0") {
            listener?.onDataPass(board[0][0])
            startingPoint = Pair(0, 0)
            finishingPoint = Pair(2, 2)
            return true
        }

        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != "0") {
            listener?.onDataPass(board[0][2])
            startingPoint = Pair(2, 0)
            finishingPoint = Pair(0, 2)
            return true
        }
        return false
    }

    fun reset() {
        currentPlayer = CellValue.X
        cells = Array(9) { Cell(CellValue.EMPTY) }
        board = Array(3) { Array(3) { "0" } }
        startingPoint = Pair(0, 0)
        finishingPoint = Pair(0, 0)
        invalidate()
    }
}