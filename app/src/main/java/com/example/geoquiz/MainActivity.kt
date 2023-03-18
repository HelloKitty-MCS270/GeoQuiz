

package com.example.geoquiz

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
//import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.geoquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity" //want to log it to see what's happening





class MainActivity : AppCompatActivity() {
    //private lateinit var true_button:Button
    //private lateinit var false_button: Button
    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()


    private val cheatLauncher = registerForActivityResult( //7.11
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
// Handle the result
        if (result.resultCode == Activity.RESULT_OK) {
            //quizViewModel.isCheater =
             //   result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            val isAnswerShown = result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            if (isAnswerShown) {
                quizViewModel.isCheater = true
                val currentQuestion = quizViewModel.questionBank[quizViewModel.currentIndex]
                currentQuestion.isCheater = true
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // hook up the button to its id
        //true_button = findViewById(R.id.true_button)
        //false_button = findViewById(R.id.false_button)

        // what happen if you click on those buttons
        binding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true, view)

        }

        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false, view)
        }

        //binding to update question by clicking the text
        binding.questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        // onset listener for the next button
        // ie what happen if you press the next button
        binding.nextButton.setOnClickListener {
            //currentIndex = (currentIndex + 1)%questionBank.size
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.cheatButton.setOnClickListener {
            // Start CheatActivity
            //val intent = Intent(this, CheatActivity:: class.java)
            //carry a piece of intent to the activity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            //startActivity(intent) //why cheatlauncher vs intent?
            cheatLauncher.launch(intent)

        }

        binding.previousButton.setOnClickListener {

            quizViewModel.moveToPrev()
            updateQuestion()

        }

        // this will get you the id for the current question in the question bank
        updateQuestion()

    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        val questionImageResId = quizViewModel.currentQuestionImage

        binding.questionTextView.setText(questionTextResId)
        binding.questionImageView.setImageResource(questionImageResId)
    }

    private fun calculateScore(){
        if (quizViewModel.questionBank.all { it.points != -1 }) {
            // calculate the score as the total points divided by the number of questions
            val totalPoints = quizViewModel.questionBank.sumOf { it.points }
            val score = (totalPoints.toDouble() / quizViewModel.questionBank.size * 100)
            val message = getString(R.string.score_message, score)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
private fun checkAnswer(userAnswer:Boolean, view: View){
    val correctAnswer = quizViewModel.currentQuestionAnswer
    val currentQuestion = quizViewModel.questionBank[quizViewModel.currentIndex]

    if (currentQuestion.points == -1) { // check if the question has not been attempted yet
        val correctAnswer = currentQuestion.answer

        if (currentQuestion.isCheater) {
            Snackbar.make(view, "Cheater! You can't handle the truth!", Snackbar.LENGTH_SHORT).show()
            currentQuestion.points = 0 // set points to 0 for cheating
        } else {
            val messageResId = if (userAnswer == correctAnswer) {
                currentQuestion.points = 1 // set points to 1 for correct answer
                R.string.correct_string
            } else {
                currentQuestion.points = 0 // set points to 0 for incorrect answer
                R.string.incorrect_string
            }
            Snackbar.make(view, messageResId, Snackbar.LENGTH_SHORT).show()
        }
        calculateScore()

    } else {
        // The question has already been attempted
        if (quizViewModel.questionBank.all { it.points != -1 }) calculateScore()

        else Snackbar.make(view, R.string.question_attempted_message, Toast.LENGTH_SHORT).show()
    }
}
}

