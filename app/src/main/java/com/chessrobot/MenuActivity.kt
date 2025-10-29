package com.chessrobot

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    private lateinit var difficultySpinner: Spinner
    private lateinit var startButton: Button
    private var selectedDifficulty = 1 // Default to medium

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Initialize views
        difficultySpinner = findViewById(R.id.menuDifficultySpinner)
        startButton = findViewById(R.id.startButton)

        // Setup difficulty spinner
        setupDifficultySpinner()

        // Setup start button
        startButton.setOnClickListener {
            startGame()
        }
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
                selectedDifficulty = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun startGame() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("DIFFICULTY", selectedDifficulty)
        startActivity(intent)
    }
}
