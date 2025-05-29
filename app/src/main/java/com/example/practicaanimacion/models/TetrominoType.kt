package com.example.practicaanimacion.models

import android.graphics.Color

enum class TetrominoType(val color: Int, val shapes: List<Array<IntArray>>) {
    I(Color.CYAN, listOf(
        arrayOf(
            intArrayOf(0, 0, 0, 0),
            intArrayOf(1, 1, 1, 1),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0)
        ),
        arrayOf(
            intArrayOf(0, 0, 1, 0),
            intArrayOf(0, 0, 1, 0),
            intArrayOf(0, 0, 1, 0),
            intArrayOf(0, 0, 1, 0)
        )
    )),
    O(Color.YELLOW, listOf(
        arrayOf(
            intArrayOf(1, 1),
            intArrayOf(1, 1)
        )
    )),
    T(Color.MAGENTA, listOf(
        arrayOf(
            intArrayOf(0, 1, 0),
            intArrayOf(1, 1, 1),
            intArrayOf(0, 0, 0)
        ),
        arrayOf(
            intArrayOf(0, 1, 0),
            intArrayOf(0, 1, 1),
            intArrayOf(0, 1, 0)
        ),
        arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(1, 1, 1),
            intArrayOf(0, 1, 0)
        ),
        arrayOf(
            intArrayOf(0, 1, 0),
            intArrayOf(1, 1, 0),
            intArrayOf(0, 1, 0)
        )
    )),
    L(Color.BLUE, listOf(
        arrayOf(
            intArrayOf(0, 0, 1),
            intArrayOf(1, 1, 1),
            intArrayOf(0, 0, 0)
        ),
        arrayOf(
            intArrayOf(0, 1, 0),
            intArrayOf(0, 1, 0),
            intArrayOf(0, 1, 1)
        ),
        arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(1, 1, 1),
            intArrayOf(1, 0, 0)
        ),
        arrayOf(
            intArrayOf(1, 1, 0),
            intArrayOf(0, 1, 0),
            intArrayOf(0, 1, 0)
        )
    )),
    J(Color.parseColor("#FFA500"), listOf(
        arrayOf(
            intArrayOf(1, 0, 0),
            intArrayOf(1, 1, 1),
            intArrayOf(0, 0, 0)
        ),
        arrayOf(
            intArrayOf(0, 1, 1),
            intArrayOf(0, 1, 0),
            intArrayOf(0, 1, 0)
        ),
        arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(1, 1, 1),
            intArrayOf(0, 0, 1)
        ),
        arrayOf(
            intArrayOf(0, 1, 0),
            intArrayOf(0, 1, 0),
            intArrayOf(1, 1, 0)
        )
    )),
    S(Color.GREEN, listOf(
        arrayOf(
            intArrayOf(0, 1, 1),
            intArrayOf(1, 1, 0),
            intArrayOf(0, 0, 0)
        ),
        arrayOf(
            intArrayOf(0, 1, 0),
            intArrayOf(0, 1, 1),
            intArrayOf(0, 0, 1)
        )
    )),
    Z(Color.RED, listOf(
        arrayOf(
            intArrayOf(1, 1, 0),
            intArrayOf(0, 1, 1),
            intArrayOf(0, 0, 0)
        ),
        arrayOf(
            intArrayOf(0, 0, 1),
            intArrayOf(0, 1, 1),
            intArrayOf(0, 1, 0)
        )
    ))
}