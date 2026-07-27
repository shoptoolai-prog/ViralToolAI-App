package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.draw.shadow
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.ElectricPurple
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.core.LanguageEngine
import com.example.creatoracademy.CreatorAcademyPrefs
import com.example.reports.ReportLanguage
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.TextWhite

/**
 * Creator Academy First Time Language Preference Selection Dialog
 * Allows first-time Creator Academy users to select English, Hindi, or Hinglish.
 */
@Composable
fun CreatorAcademyLanguageDialog(
    onLanguageConfirmed: (String) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val currentSetup = remember { CreatorAcademyPrefs.getSetupData(context) }
    var selectedOption by remember { mutableStateOf(currentSetup.preferredLanguage) } // "English", "Hindi", "HinEnglish"

    val languages = listOf(
        Triple("English", "🇬🇧 English", "All lessons, AI explanations & scripts in English"),
        Triple("Hindi", "🇮🇳 Hindi (हिंदी)", "सभी सबक, एआई व्याख्या और टिप्स हिंदी में"),
        Triple("HinEnglish", "🇮🇳 Hinglish (HinEnglish)", "Sabhi lessons, AI explanations aur tips Hinglish me")
    )

    Dialog(
        onDismissRequest = { /* Modal: require explicit selection */ },
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.80f))
                .navigationBarsPadding()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color(0xFF0F1A14),
                border = BorderStroke(
                    1.5.dp,
                    Brush.linearGradient(
                        listOf(EmeraldGlow, ElectricPurple.copy(alpha = 0.6f), EmeraldPrimary)
                    )
                ),
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth(0.92f)
                    .shadow(24.dp, RoundedCornerShape(28.dp), spotColor = EmeraldGlow)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header Icon
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0x2210B981))
                            .border(BorderStroke(1.2.dp, EmeraldPrimary), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Which language would you like to learn in?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Creator Academy lessons, AI tips, hooks, and generated answers will appear in your chosen language.",
                        fontSize = 12.sp,
                        color = TextWhite.copy(alpha = 0.65f),
                        textAlign = TextAlign.Center,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Language Cards
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        languages.forEach { (code, label, desc) ->
                            val isSelected = selectedOption == code
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) Color(0x2210B981) else Color(0x0CFFFFFF))
                                    .border(
                                        BorderStroke(
                                            if (isSelected) 1.5.dp else 1.dp,
                                            if (isSelected) EmeraldPrimary else Color(0x1AFFFFFF)
                                        ),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        selectedOption = code
                                    }
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = label,
                                            fontSize = 14.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) EmeraldPrimary else TextWhite
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = desc,
                                            fontSize = 11.sp,
                                            color = TextWhite.copy(alpha = 0.6f)
                                        )
                                    }

                                    if (isSelected) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(EmeraldPrimary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = AmoledBlack,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // Confirm Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(EmeraldPrimary)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                // Save preferences
                                val updatedSetup = currentSetup.copy(preferredLanguage = selectedOption)
                                CreatorAcademyPrefs.saveSetupData(context, updatedSetup)
                                CreatorAcademyPrefs.setLanguageSelected(context, true)

                                val reportLang = when (selectedOption) {
                                    "Hindi" -> ReportLanguage.HINDI
                                    "HinEnglish", "Hinglish" -> ReportLanguage.HINGLISH
                                    else -> ReportLanguage.ENGLISH
                                }
                                LanguageEngine.setLanguage(context, reportLang)
                                onLanguageConfirmed(selectedOption)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = AmoledBlack,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Start Learning in ${if (selectedOption == "Hindi") "Hindi" else if (selectedOption == "HinEnglish") "Hinglish" else "English"} 🚀",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Black,
                                color = AmoledBlack
                            )
                        }
                    }
                }
            }
        }
    }
}
