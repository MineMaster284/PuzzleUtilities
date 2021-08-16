package com.collinscoding.puzzle.kakuro

import kotlin.math.log10

class Kakuro {

}

data class KakuroBorder(val rowSum: Int, val colSum: Int) {
    override fun toString(): String {
        var final = ""
        if (log10(rowSum.toDouble()).toInt() < 2) {
            final += " "
            final += rowSum
        } else {
            final += rowSum
        }
        final += '\\'
        if (log10(colSum.toDouble()).toInt() < 2) {
            final += " "
            final += colSum
        } else {
            final += colSum
        }
        return final
    }
}