package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyFromPrefs(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val savedName = savedInstanceState?.getString("name")
            ?: intent.getStringExtra("user_name")
            ?: ""
        if (savedName.isNotEmpty()) binding.etName.setText(savedName)
        binding.switchTheme.setOnCheckedChangeListener(null)
        binding.switchTheme.isChecked = ThemeManager.isDark(this)
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            ThemeManager.setDark(this, isChecked)
            recreate()
        }

        binding.btnStart.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name to start", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(
                Intent(this, QuizActivity::class.java).putExtra("user_name", name)
            )
        }
    }
    override fun onSaveInstanceState(out: Bundle) {
        super.onSaveInstanceState(out)
        out.putString("name", binding.etName.text.toString())
    }
}
