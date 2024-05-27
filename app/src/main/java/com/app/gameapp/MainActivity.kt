package com.app.gameapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.notification.ZenPolicy
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var puzzleGrid: GridLayout
    private lateinit var shuffleButton: Button
    private lateinit var helpMeButton: Button
    private lateinit var statusTextView: TextView
    private val tiles = Array(16){ i -> if (i == 15) 0 else i + 1}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        puzzleGrid = findViewById(R.id.puzzle_grid)
        shuffleButton = findViewById(R.id.shuffle_button)
        helpMeButton = findViewById(R.id.helpMe_button)
        statusTextView = findViewById(R.id.status_text_view)

        shuffleButton.setOnClickListener{ shuffleTiles()}
        helpMeButton.setOnClickListener{ makeMove() }

        initializeGrid()

    }

    private fun initializeGrid(){
        puzzleGrid.columnCount = 4
        puzzleGrid.rowCount = 4
        updateGrid()
    }

    private fun shuffleTiles() {
        tiles.shuffle()
        if(!isSolvable()){
            statusTextView.text = "Not Solvable"
            Toast.makeText(this, "Not Sovable", Toast.LENGTH_SHORT).show()

        } else {
            statusTextView.text = "Solvable"
            Toast.makeText(this, "Solvable", Toast.LENGTH_SHORT).show()
        }
        updateGrid()
    }

    private fun updateGrid() {
        puzzleGrid.removeAllViews()
        tiles.forEach { tile ->
            val tileView = TextView(this).apply{
                text = if (tile == 0 ) " " else tile.toString()
                textSize = 24f

                setPadding(16, 16,16, 16)
            }
            puzzleGrid.addView(tileView)
        }
    }

    private fun isSolvable(): Boolean {
        var inversions = 0
        for (i in tiles.indices){
            for (j in i + 1 until tiles.size){
                if(tiles[i] != 0 && tiles[j] != 0 && tiles[i] > tiles[j])
                    inversions++
            }
        }
        return inversions % 2 ==0
    }

    private fun makeMove(){
        val zeroIndex = tiles.indexOf(0)
        val possibleMoves = getPossibleMoves(zeroIndex)

        if (possibleMoves.isNotEmpty()){
            val move = possibleMoves[Random.nextInt(possibleMoves.size)]
            tiles[zeroIndex] = tiles[move]
            tiles[move] = 0
            updateGrid()
        }
        if(isSolved())
            statusTextView.text = "Puzzle Solved"
    }

    private fun getPossibleMoves(zeroIndex: Int) : List<Int>{
        val moves = mutableListOf<Int>()
        val row = zeroIndex /4
        val col = zeroIndex % 4

        if(row > 0) moves.add(zeroIndex - 4) // Up
        if (row < 3) moves.add(zeroIndex + 4) //down
        if (col > 0) moves.add(zeroIndex - 1) // Right
        if (col < 3 ) moves.add(zeroIndex + 1) //Left

        return moves
    }

    private fun isSolved(): Boolean{
        for (i in 1..14){
            if(tiles[i - 1]!= i)
                return false
        }

        return tiles[15] ==0
    }
}