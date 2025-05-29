package com.example.practicaanimacion.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.practicaanimacion.models.*
import com.example.practicaanimacion.observer.Observer

class TetrisBoard(context: Context?, attrs: AttributeSet?) : View(context, attrs), Observer {

    private var currentTetromino: Tetromino? = null
    private var onScoreUpdateListener: ((Int, Int) -> Unit)? = null
    private val board = Array(20) { Array(10) { 0 } }

    private var score = 0
    private var level = 1
    private var gameState = GameState.RUNNING
    private var gameSpeed = 800L

    private val handler = Handler(Looper.getMainLooper())
    private val gameLoop = object : Runnable {
        override fun run() {
            if (gameState == GameState.RUNNING) {
                moveDown()
                handler.postDelayed(this, gameSpeed)
            }
        }
    }

    private val paintGrid = Paint().apply {
        color = Color.DKGRAY
        style = Paint.Style.STROKE
        strokeWidth = 1.5f
    }

    private val paintCell = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val ghostPaint = Paint().apply {
        style = Paint.Style.FILL
        alpha = 70
        isAntiAlias = true
    }

    private var cellSize = 0f
    private var offsetY = 0f
    private val padding = 20f * context!!.resources.displayMetrics.density

    private var onGameOverListener: ((score: Int) -> Unit)? = null

    init {
        setBackgroundColor(Color.BLACK)
        startGame()
    }

    private fun startGame() {
        score = 0
        level = 1
        gameSpeed = 800L
        gameState = GameState.RUNNING
        clearBoard()
        spawnNewTetromino()
        handler.post(gameLoop)
    }

    private fun clearBoard() {
        for (i in board.indices) {
            for (j in board[i].indices) {
                board[i][j] = 0
            }
        }
    }

    private fun spawnNewTetromino() {
        currentTetromino = Tetromino.createRandom()
        if (!currentTetromino!!.canMove(0, 0, board)) {
            gameState = GameState.GAME_OVER
            handler.removeCallbacks(gameLoop)
            onGameOverListener?.invoke(score)
        } else {
            currentTetromino!!.addObserver(this)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        cellSize = (width - 2 * padding) / 10f
        val boardHeight = cellSize * 20f
        offsetY = (height - boardHeight) / 2f

        canvas.save()
        canvas.translate(padding, offsetY)

        drawBoard(canvas)
        drawGhostPiece(canvas)
        currentTetromino?.draw(canvas, cellSize, 0f)

        canvas.restore()

    }

    private fun drawBoard(canvas: Canvas) {
        for (i in board.indices) {
            for (j in board[i].indices) {
                val left = j * cellSize
                val top = i * cellSize
                if (board[i][j] != 0) {
                    paintCell.color = board[i][j]
                    canvas.drawRect(left, top, left + cellSize, top + cellSize, paintCell)
                }
                canvas.drawRect(left, top, left + cellSize, top + cellSize, paintGrid)
            }
        }
    }

    private fun drawGhostPiece(canvas: Canvas) {
        val ghost = currentTetromino ?: return
        var yOffset = 0
        while (ghost.canMove(0, yOffset + 1, board)) {
            yOffset++
        }
        val shape = ghost.getShape()
        ghostPaint.color = ghost.getColor()
        ghostPaint.alpha = 70
        for (i in shape.indices) {
            for (j in shape[i].indices) {
                if (shape[i][j] == 1) {
                    val x = ghost.xCell + j
                    val y = ghost.yCell + i + yOffset
                    val left = x * cellSize
                    val top = y * cellSize
                    canvas.drawRect(left, top, left + cellSize, top + cellSize, ghostPaint)
                }
            }
        }
    }

    private fun moveDown() {
        val tetromino = currentTetromino ?: return
        if (!tetromino.move(Direction.DOWN, board)) {
            tetromino.lockToBoard(board)
            val linesCleared = clearLines()
            if (linesCleared > 0) {
                val comboMultiplier = if (linesCleared >= 2) linesCleared else 1
                score += linesCleared * comboMultiplier * 10
            }

            if (score / 5000 + 1 > level) {
                level++
                gameSpeed = (gameSpeed * 0.85).toLong().coerceAtLeast(100L)
            }
            onScoreUpdateListener?.invoke(score, level)
            spawnNewTetromino()
        }
        invalidate()
    }


    private fun clearLines(): Int {
        var linesCleared = 0
        var i = board.size - 1
        while (i >= 0) {
            if (board[i].all { it != 0 }) {
                for (k in i downTo 1) {
                    board[k] = board[k - 1].clone()
                }
                board[0] = Array(10) { 0 }
                linesCleared++
            } else {
                i--
            }
        }
        return linesCleared
    }


    private fun dropPiece() {
        val tetromino = currentTetromino ?: return
        while (tetromino.move(Direction.DOWN, board)) {}
        moveDown()
    }

    private fun moveLeft() {
        currentTetromino?.move(Direction.LEFT, board)
        invalidate()
    }

    private fun moveRight() {
        currentTetromino?.move(Direction.RIGHT, board)
        invalidate()
    }

    private fun rotate() {
        currentTetromino?.move(Direction.ROTATE, board)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && gameState == GameState.RUNNING) {
            val x = event.x
            val y = event.y

            when {
                x < width * 0.4f -> moveLeft()
                x > width * 0.6f -> moveRight()
            }
        }
        return true
    }

    override fun update() {
        invalidate()
    }

    fun setOnGameOverListener(listener: (Int) -> Unit) {
        this.onGameOverListener = listener
    }

    fun performDrop() {
        dropPiece()
    }

    fun performRotate() {
        rotate()
    }

    fun setOnScoreUpdateListener(listener: (score: Int, level: Int) -> Unit) {
        onScoreUpdateListener = listener
    }

}
