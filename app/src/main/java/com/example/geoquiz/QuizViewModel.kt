package com.example.geoquiz
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {


    val questionBank = listOf(
        Question(R.string.q1, R.drawable.q1, true),
        Question(R.string.q2, R.drawable.q2, false),
        Question(R.string.q3, R.drawable.q3, false),
        Question(R.string.q4, R.drawable.q4, false),
        Question(R.string.q5, R.drawable.q5, false)
    )

    var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    val currentQuestionImage: Int
        get() = questionBank[currentIndex].imageResId

    fun moveToNext() {
        Log.d(TAG, "Updating question text", Exception())
        currentIndex = (currentIndex + 1) % questionBank.size

    }

    fun moveToPrev() {
            currentIndex = (currentIndex - 1)
            if (currentIndex < 0) {
                currentIndex = questionBank.size - 1
            }
        }

}