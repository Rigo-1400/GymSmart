package com.example.gymsmart

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),  // Light Purple
    secondary = Color(0xFF03DAC6),  // Teal
    background = Color(0xFF121212),  // Very dark gray (almost black)
    surface = Color(0xFF1E1E1E),    // Dark gray for cards/menus
    onPrimary = Color.White,         // Text/icon on primary color
    onSecondary = Color.Black,       // Text/icon on secondary color
    onBackground = Color(0xFFE0E0E0),  // Light gray text on background
    onSurface = Color(0xFFE0E0E0)    // Light gray text on surfaces
)
val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),     // Deep Purple
    secondary = Color(0xFF03DAC6),   // Teal
    background = Color(0xFFFFFFFF),  // White
    surface = Color(0xFFFFFFFF),     // White
    onPrimary = Color.White,         // Text/icon on primary color
    onSecondary = Color.Black,       // Text/icon on secondary color
    onBackground = Color.Black,      // Text/icon on background color
    onSurface = Color.Black          // Text/icon on surface color
)

