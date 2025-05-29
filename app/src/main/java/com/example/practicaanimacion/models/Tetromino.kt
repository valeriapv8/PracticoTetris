package com.example.practicaanimacion.models

import android.graphics.Canvas
import android.graphics.Paint
import com.example.practicaanimacion.observer.Observable
import com.example.practicaanimacion.observer.Observer

class Tetromino(private val type: TetrominoType) : Observable {

    private var observer: Observer? = null
    private var rotation = 0

    var xCell: Int = 3
    var yCell: Int = 0

    private val shapes = type.shapes
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = type.color
    }

    fun draw(canvas: Canvas, cellSize: Float, offsetY: Float) {
        val shape = shapes[rotation]
        for (i in shape.indices) {
            for (j in shape[i].indices) {
                if (shape[i][j] == 1) {
                    val left = (xCell + j) * cellSize
                    val top = offsetY + (yCell + i) * cellSize
                    canvas.drawRect(left, top, left + cellSize, top + cellSize, paint)
                }
            }
        }
    }


    fun move(direction: Direction, board: Array<Array<Int>>): Boolean {
        return when (direction) {
            Direction.LEFT -> if (canMove(-1, 0, board)) { xCell--; true } else false
            Direction.RIGHT -> if (canMove(1, 0, board)) { xCell++; true } else false
            Direction.DOWN -> if (canMove(0, 1, board)) { yCell++; true } else false
            Direction.ROTATE -> {
                val newRotation = (rotation + 1) % shapes.size
                if (canRotate(newRotation, board)) { rotation = newRotation; true } else false
            }
            else -> false
        }
    }

    fun canMove(dx: Int, dy: Int, board: Array<Array<Int>>): Boolean {
        val shape = shapes[rotation]
        for (i in shape.indices) {
            for (j in shape[i].indices) {
                if (shape[i][j] == 1) {
                    val newX = xCell + j + dx
                    val newY = yCell + i + dy
                    if (newY >= board.size || newX !in board[0].indices || newY < 0 || board[newY][newX] != 0) {
                        return false
                    }
                }
            }
        }
        return true
    }

    fun canRotate(newRotation: Int, board: Array<Array<Int>>): Boolean {
        val shape = shapes[newRotation]
        for (i in shape.indices) {
            for (j in shape[i].indices) {
                if (shape[i][j] == 1) {
                    val newX = xCell + j
                    val newY = yCell + i
                    if (newX !in board[0].indices || newY !in board.indices || board[newY][newX] != 0) {
                        return false
                    }
                }
            }
        }
        return true
    }

    fun lockToBoard(board: Array<Array<Int>>) {
        val shape = shapes[rotation]
        for (i in shape.indices) {
            for (j in shape[i].indices) {
                if (shape[i][j] == 1) {
                    val boardX = xCell + j
                    val boardY = yCell + i
                    if (boardY in board.indices && boardX in board[0].indices) {
                        board[boardY][boardX] = type.color
                    }
                }
            }
        }
    }

    fun getColor(): Int {
        return paint.color
    }

    fun getShape(): Array<IntArray> {
        return shapes[rotation % shapes.size]
    }

    override fun addObserver(o: Observer) {
        observer = o
    }

    override fun notifyObservers() {
        observer?.update()
    }


    companion object {
        fun createRandom(): Tetromino {
            val randomType = TetrominoType.values().random()
            return Tetromino(randomType)
        }
    }
}
