package com.collinscoding.puzzle.minesweeper

import java.io.File

class Minesweeper(val size: Int) {
    val grid = Array(size) { Array<CellState>(size) { CellState.Blank } }
    private val guesses = mutableListOf<Pair<Int, Int>>()

    fun parse(file: File) {
        file.readLines().forEachIndexed { lineI, line ->
            if (lineI >= size) error("Too many lines in the file.")
            line.forEachIndexed { rowI, c ->
                when (c) {
                    ' ' -> grid[rowI][lineI] = CellState.Empty
                    'X' -> grid[rowI][lineI] = CellState.Mine
                    '-' -> grid[rowI][lineI] = CellState.Blank
                    else -> grid[rowI][lineI] = CellState.of(c.toString().toInt())
                }
            }
        }
    }

    fun setCell(x: Int, y: Int, value: CellState) {
        grid[x][y] = value
    }

    fun getGuesses(): List<Pair<Int, Int>> {
        checkMines()
        return guesses
    }

    fun getMines(): List<Pair<Int, Int>> {
        while (checkMines()) {}
        return (0 until size).flatMap { x -> (0 until size).map { y -> x to y } }
            .filter { (x, y) -> grid[x][y] == CellState.Mine }
    }

    fun checkMines(): Boolean {
        var changed = false
        for (x in 0 until size) {
            for (y in 0 until size) {
                val cellState = grid[x][y]
                if (cellState is CellState.Number) {
                    val possibilities = directions.filter { (dx, dy) -> (x + dx) in 0 until size && (y + dy) in 0 until size }
                        .map { (dx, dy) -> (x + dx) to (y + dy) }
                    if (possibilities.count { (dx, dy) -> grid[dx][dy] == CellState.Mine } == cellState.n) {
                        guesses.addAll(possibilities.filter { grid[it.first][it.second] == CellState.Blank })
                    }
                    else if (possibilities.count { (dx, dy) -> grid[dx][dy] == CellState.Mine || grid[dx][dy] == CellState.Blank } == cellState.n) {
                        possibilities.filter { grid[it.first][it.second] == CellState.Blank }.forEach { setCell(it.first, it.second, CellState.Mine) }
                        changed = true
                    }
                }
            }
        }
        return changed
    }

    companion object {
        private val directions = listOf(
            -1 to -1,
            -1 to 0,
            -1 to 1,
            0 to -1,
            0 to 1,
            1 to -1,
            1 to 0,
            1 to 1
        )
    }
}

sealed class CellState {
    object Blank : CellState()
    object Empty : CellState()
    object Mine : CellState()
    class Number internal constructor(val n: Int) : CellState() {
        companion object {
            internal val numMap = mutableMapOf<Int, Number>()
        }
    }

    companion object {
        fun of(n: Int) = Number.numMap.getOrPut(n) { Number(n) }
    }
}
