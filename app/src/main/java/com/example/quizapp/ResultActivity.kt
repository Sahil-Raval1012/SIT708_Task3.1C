package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    private var score    = 0
    private var total    = 0
    private var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyFromPrefs(this)
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState != null) {
            score    = savedInstanceState.getInt("score",  0)
            total    = savedInstanceState.getInt("total",  10)
            userName = savedInstanceState.getString("userName") ?: ""
        }
        if (userName.isEmpty()) {
            score    = intent.getIntExtra("score",     0)
            total    = intent.getIntExtra("total",     10)
            userName = intent.getStringExtra("user_name") ?: "Player"
        }
        binding.tvResultName.text = "Congratulations, $userName!"
        binding.tvScore.text      = "$score / $total"
        binding.switchTheme.setOnCheckedChangeListener(null)
        binding.switchTheme.isChecked = ThemeManager.isDark(this)
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            ThemeManager.setDark(this, isChecked)
            recreate()
        }
        binding.btnTakeNewQuiz.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
                    .putExtra("user_name", userName)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }
        binding.btnFinish.setOnClickListener {
            finishAffinity()
        }
    }
    override fun onSaveInstanceState(out: Bundle) {
        super.onSaveInstanceState(out)
        out.putInt("score",  score)
        out.putInt("total",  total)
        out.putString("userName", userName)
    }
}
