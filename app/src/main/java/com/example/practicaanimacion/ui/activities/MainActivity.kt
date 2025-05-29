package com.example.practicaanimacion.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.practicaanimacion.databinding.ActivityMainBinding
import com.example.practicaanimacion.databinding.DialogGameOverBinding
import com.example.practicaanimacion.db.AppDatabase
import com.example.practicaanimacion.db.dao.ScoreEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tetrisBoard = binding.tetrisBoard

        binding.btnDrop.setOnClickListener {
            tetrisBoard.performDrop()
        }

        binding.btnRotate.setOnClickListener {
            tetrisBoard.performRotate()
        }

        tetrisBoard.setOnScoreUpdateListener { score, level ->
            binding.tvScore.text = "Score: $score"
            binding.tvLevel.text = "Level: $level"
        }

        tetrisBoard.setOnGameOverListener { score ->
            showGameOverDialog(score)
        }
    }

    private fun showGameOverDialog(score: Int) {
        val dialogBinding = DialogGameOverBinding.inflate(layoutInflater)

        dialogBinding.tvFinalScore.text = "Tu puntaje: $score"

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialogBinding.btnSaveScore.setOnClickListener {
            val name = dialogBinding.etPlayerName.text.toString().trim()
            if (name.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val db = AppDatabase.getDatabase(applicationContext)
                    val exists = db.scoreDao().existsByName(name)
                    withContext(Dispatchers.Main) {
                        if (exists > 0) {
                            dialogBinding.etPlayerName.error = "Este nombre ya existe"
                        } else {
                            CoroutineScope(Dispatchers.IO).launch {
                                db.scoreDao().insert(ScoreEntity(playerName = name, score = score))
                                withContext(Dispatchers.Main) {
                                    dialog.dismiss()
                                    startActivity(Intent(this@MainActivity, HighScoresActivity::class.java))
                                    finish()
                                }
                            }
                        }
                    }
                }
            } else {
                dialogBinding.etPlayerName.error = "Ingresa tu nombre"
            }
        }

        dialog.show()
    }
}
