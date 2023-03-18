package com.example.geoquiz

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, @DrawableRes val imageResId: Int, val answer: Boolean, var points: Int = -1, var isCheater: Boolean = false)


