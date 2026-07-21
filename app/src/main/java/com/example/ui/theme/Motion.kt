package com.example.omnimind.ui.theme

import androidx.compose.animation.core.tween

// Removing conflicting MotionDuration as it's not a standard Material3 class in this way
// and caused recursive type checking issues.

object AppMotion {
    const val Fast = 100
    const val Medium = 200
    const val Slow = 300
}
