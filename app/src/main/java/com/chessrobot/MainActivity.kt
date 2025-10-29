package com.chessrobot

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.move.Move
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var chessBoardView: ChessBoardView
    private lateinit var statusText: TextView
    private lateinit var difficultySpinner: Spinner
    private lateinit var newGameButton: Button
    private lateinit var resetButton: Button
    
    private lateinit var chessEngine: SimpleChessEngine
    private val mainScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        chessBoardView = findViewById(R.id.chessBoardView)
        statusText = findViewById(R.id.statusText)
        difficultySpinner = findViewById(R.id.difficultySpinner)
        newGameButton = findViewById(R.id.newGameButton)
        resetButton = findViewById(R.id.resetButton)

        // Setup difficulty spinner
        setupDifficultySpinner()

        // Get difficulty from intent
        val difficulty = intent.getIntExtra("DIFFICULTY", 1)
        difficultySpinner.setSelection(difficulty)

        // Initialize chess engine
        chessEngine = SimpleChessEngine()
        chessEngine.setDifficulty(difficulty)

        // Setup chess board callback
        chessBoardView.setMoveCallback { from, to ->
            handlePlayerMove(from, to)
        }

        // Setup buttons
        newGameButton.setOnClickListener {
            startNewGame()
        }

        resetButton.setOnClickListener {
            resetBoard()
        }

        // Start new game
        startNewGame()
    }

    private fun setupDifficultySpinner() {
        val difficulties = arrayOf(
            getString(R.string.difficulty_easy),
            getString(R.string.difficulty_medium),
            getString(R.string.difficulty_hard),
            getString(R.string.difficulty_expert)
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficulties)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        difficultySpinner.adapter = adapter
        difficultySpinner.setSelection(1) // Default to medium

        difficultySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                chessEngine.setDifficulty(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun startNewGame() {
        chessEngine.reset()
        chessBoardView.setBoard(chessEngine.board)
        updateStatus(getString(R.string.your_turn))
        enableControls(true)
    }

    private fun resetBoard() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.reset))
            .setMessage("Oyunu sıfırlamak istediğinize emin misiniz?")
            .setPositiveButton("Evet") { _, _ ->
                startNewGame()
            }
            .setNegativeButton("Hayır", null)
            .show()
    }

    private fun handlePlayerMove(from: Int, to: Int) {
        if (!chessEngine.isPlayerTurn()) {
            Toast.makeText(this, "Robot'un sırası!", Toast.LENGTH_SHORT).show()
            return
        }

        val moveSuccess = chessEngine.makePlayerMove(from, to)
        
        if (moveSuccess) {
            chessBoardView.invalidate()
            
            if (checkGameOver()) {
                return
            }

            // Robot's turn
            updateStatus(getString(R.string.robot_thinking))
            enableControls(false)
            
            mainScope.launch {
                delay(500) // Visual delay for better UX
                
                withContext(Dispatchers.Default) {
                    chessEngine.makeRobotMove()
                }
                
                chessBoardView.invalidate()
                
                if (checkGameOver()) {
                    return@launch
                }
                
                updateStatus(getString(R.string.your_turn))
                enableControls(true)
            }
        } else {
            Toast.makeText(this, getString(R.string.invalid_move), Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkGameOver(): Boolean {
        if (chessEngine.isGameOver()) {
            val result = chessEngine.getGameResult()
            var message = ""
            
            when {
                result.contains("1-0") -> message = getString(R.string.you_win)
                result.contains("0-1") -> message = getString(R.string.robot_wins)
                else -> message = getString(R.string.draw)
            }
            
            updateStatus("${getString(R.string.game_over)}: $message")
            enableControls(false)
            
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.game_over))
                .setMessage(message)
                .setPositiveButton(getString(R.string.new_game)) { _, _ ->
                    startNewGame()
                }
                .setCancelable(false)
                .show()
            
            return true
        }
        return false
    }

    private fun updateStatus(status: String) {
        statusText.text = status
    }

    private fun enableControls(enable: Boolean) {
        chessBoardView.isEnabled = enable
        difficultySpinner.isEnabled = enable
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}
