package com.example.omnimind.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * OmniMind Bold Aesthetic: Cyber-Industrial / Brutal Minimalism
 * Focus: High contrast, technical precision, grain texture, and asymmetric flow.
 */

// The Void - Primary Base
val VoidBlack = Color(0xFF020202)
val IndustrialGrey = Color(0xFF0D0D0D)
val SteelBorder = Color(0xFF1A1A1A)

// Neon Signal - Accents
val SignalGreen = Color(0xFF00FF41) // Classic Terminal Green
val SignalGreenDim = Color(0x3300FF41)
val SignalRed = Color(0xFFFF0033) // Sharp Warning Red

// Typography & Content
val RawWhite = Color(0xFFF0F0F0)
val GhostGrey = Color(0xFF444444)
val DeepMuted = Color(0xFF222222)

// Textures & Overlays
val GrainOverlay = Color(0x0DFFFFFF)
val ScanlineColor = Color(0x0500FF41)

// Legacy Mapping (Redefined for the new bold aesthetic)
val ObsidianPrimary = SignalGreen
val ObsidianSecondary = RawWhite
val ObsidianBackground = VoidBlack
val ObsidianSurface = IndustrialGrey
val ObsidianOnSurface = RawWhite

// Theme Overrides for standard Material 3 mapping
val Purple80 = SignalGreen
val PurpleGrey80 = GhostGrey
val Pink80 = SignalRed
val Purple40 = SignalGreen
val PurpleGrey40 = DeepMuted
val Pink40 = SignalRed
