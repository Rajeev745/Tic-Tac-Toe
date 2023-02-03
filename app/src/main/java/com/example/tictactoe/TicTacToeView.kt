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
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var listener: OnDataPass? = null
    private var cellHeight = 0f
    private var cellWidth = 0f
    private var currentPlayer = CellValue.X

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

    private var cells = arrayOf(
        Cell(CellValue.EMPTY),
        Cell(CellValue.EMPTY),
        Cell(CellValue.EMPTY),
        Cell(CellValue.EMPTY),
        Cell(CellValue.EMPTY),
        Cell(CellValue.EMPTY),
        Cell(CellValue.EMPTY),
        Cell(CellValue.EMPTY),
        Cell(CellValue.EMPTY)
    )

    private var board = arrayOf(arrayOf(0, 0, 0), arrayOf(0, 0, 0), arrayOf(0, 0, 0))

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
    }

    private fun drawGrid(canvas: Canvas?) {
        for (i in 1..2) {
            if (canvas != null) {
                canvas.drawLine(cellWidth * i, 0f, cellWidth * i, canvas.height.toFloat(), paint)
            }
            if (canvas != null) {
                canvas.drawLine(0f, cellHeight * i, canvas.width.toFloat(), cellHeight * i, paint)
            }
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
                        if (currentPlayer.toString() == "X") board[row][column] =
                            1 else board[row][column] = 2

                        currentPlayer =
                            if (currentPlayer == CellValue.X) CellValue.O else CellValue.X

                        invalidate()
                        checkWin()
                    }
                }
            }
        }
        return true
    }

    fun checkWin() {
        val ticTacToeFragment = TicTacToeFragment()
        for (i in 0..2) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != 0) {
                listener?.onDataPass(board[i][0].toString())
                return
            }
        }

        for (i in 0..2) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != 0) {
                listener?.onDataPass(board[0][i].toString())
                return
            }
        }

        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != 0) {
            listener?.onDataPass(board[0][0].toString())
            return
        }

        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != 0) {
            listener?.onDataPass(board[0][2].toString())
            return
        }
    }

    fun reset() {
        currentPlayer = CellValue.X
        cells = Array(9) { Cell(CellValue.EMPTY) }
        invalidate()
    }
}