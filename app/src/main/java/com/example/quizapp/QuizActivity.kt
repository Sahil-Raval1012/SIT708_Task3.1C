package com.example.quizapp
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.databinding.ActivityQuizBinding
import com.google.android.material.button.MaterialButton
class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private val questions = QuizData.questions
    private lateinit var optionButtons: List<MaterialButton>

    // State variables — survive dark-mode recreate()
    private var currentIndex = 0
    private var score        = 0
    private var selected     = -1
    private var submitted    = false
    private var userName     = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyFromPrefs(this)
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        optionButtons = listOf(
            binding.btnOption1,
            binding.btnOption2,
            binding.btnOption3,
            binding.btnOption4
        )
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt("currentIndex", 0)
            score        = savedInstanceState.getInt("score", 0)
            selected     = savedInstanceState.getInt("selected", -1)
            submitted    = savedInstanceState.getBoolean("submitted", false)
            userName     = savedInstanceState.getString("userName") ?: ""
        }
        if (userName.isEmpty()) {
            userName = intent.getStringExtra("user_name") ?: "Player"
        }
        binding.switchTheme.setOnCheckedChangeListener(null)
        binding.switchTheme.isChecked = ThemeManager.isDark(this)
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            ThemeManager.setDark(this, isChecked)
            recreate()
        }
        optionButtons.forEachIndexed { i, btn ->
            btn.setOnClickListener {
                if (!submitted) {
                    selected = i
                    renderOptions()
                }
            }
        }
        binding.btnSubmit.setOnClickListener {
            if (selected == -1) {
                Toast.makeText(this, "Please select an answer first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!submitted) {
                submitAnswer()
            }
        }


        binding.btnNext.setOnClickListener {
            currentIndex++
            if (currentIndex < questions.size) {
                submitted = false
                selected  = -1
                renderQuestion()
                renderOptions()
            } else {
                startActivity(
                    Intent(this, ResultActivity::class.java)
                        .putExtra("score",     score)
                        .putExtra("total",     questions.size)
                        .putExtra("user_name", userName)
                )
                finish()
            }
        }
        renderQuestion()
        if (submitted) {
            binding.progressBar.progress = (currentIndex + 1) * 100 / questions.size
            renderOptions()
            renderFeedback()
            binding.btnSubmit.visibility = View.GONE
            binding.btnNext.visibility   = View.VISIBLE
            binding.btnNext.text         = nextButtonLabel()
        } else {
            renderOptions()
        }
    }

    override fun onSaveInstanceState(out: Bundle) {
        super.onSaveInstanceState(out)
        out.putInt("currentIndex", currentIndex)
        out.putInt("score",        score)
        out.putInt("selected",     selected)
        out.putBoolean("submitted", submitted)
        out.putString("userName",  userName)
    }

    private fun renderQuestion() {
        val q = questions[currentIndex]
        binding.tvQuestion.text  = q.text
        binding.tvProgress.text  = "Question ${currentIndex + 1} of ${questions.size}"
        binding.progressBar.progress = currentIndex * 100 / questions.size

        optionButtons.forEachIndexed { i, btn ->
            btn.text      = q.options[i]
            btn.isEnabled = true
        }

        binding.tvFeedback.visibility = View.GONE
        binding.btnSubmit.visibility  = View.VISIBLE
        binding.btnNext.visibility    = View.GONE
    }
    private fun renderOptions() {
        val correct = questions[currentIndex].answerIndex
        optionButtons.forEachIndexed { i, btn ->
            when {
                submitted && i == correct -> {
                    tintButton(btn, COLOR_GREEN, Color.WHITE)
                    btn.isEnabled = false
                }
                submitted && i == selected && i != correct -> {
                    tintButton(btn, COLOR_RED, Color.WHITE)
                    btn.isEnabled = false
                }
                submitted -> {
                    tintButton(btn, Color.TRANSPARENT, defaultText())
                    btn.isEnabled = false
                }
                i == selected -> {
                    tintButton(btn, COLOR_BLUE, Color.WHITE)
                }
                else -> {
                    tintButton(btn, Color.TRANSPARENT, defaultText())
                }
            }
        }
    }
    private fun renderFeedback() {
        if (selected == -1) return
        val correct = questions[currentIndex].answerIndex
        if (selected == correct) {
            binding.tvFeedback.text = "✓  Correct!"
            binding.tvFeedback.setBackgroundColor(Color.parseColor("#1B5E20"))
            binding.tvFeedback.setTextColor(Color.WHITE)
        } else {
            binding.tvFeedback.text =
                "✗  Wrong!  Correct answer: ${questions[currentIndex].options[correct]}"
            binding.tvFeedback.setBackgroundColor(Color.parseColor("#B71C1C"))
            binding.tvFeedback.setTextColor(Color.WHITE)
        }
        binding.tvFeedback.visibility = View.VISIBLE
    }
    private fun submitAnswer() {
        submitted = true
        val correct = questions[currentIndex].answerIndex
        if (selected == correct) score++
        binding.progressBar.progress = (currentIndex + 1) * 100 / questions.size
        renderOptions()
        renderFeedback()
        binding.btnSubmit.visibility = View.GONE
        binding.btnNext.visibility   = View.VISIBLE
        binding.btnNext.text         = nextButtonLabel()
    }
    private fun nextButtonLabel() =
        if (currentIndex == questions.size - 1) "See Results" else "Next"

    private fun tintButton(btn: MaterialButton, bgColor: Int, textColor: Int) {
        btn.backgroundTintList = ColorStateList.valueOf(bgColor)
        btn.setTextColor(textColor)
    }
    private fun defaultText() =
        if (ThemeManager.isDark(this)) Color.parseColor("#ECEFF1")
        else Color.parseColor("#1A237E")
    companion object {
        private val COLOR_GREEN = Color.parseColor("#2E7D32")
        private val COLOR_RED   = Color.parseColor("#C62828")
        private val COLOR_BLUE  = Color.parseColor("#1565C0")
    }
}
