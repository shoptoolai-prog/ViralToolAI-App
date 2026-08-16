package com.example

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.MyApplicationTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ViralToolAI", appName)
  }

  @Test
  fun `verify all required drawables exist and load without crashing`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    
    val drawables = listOf(
        R.drawable.hero_creator_banner,
        R.drawable.ai_reel_analysis,
        R.drawable.ai_creator_assistant,
        R.drawable.thumbnail_picker,
        R.drawable.subtitles_generator,
        R.drawable.voice_cleaner,
        R.drawable.smart_video_text,
        R.drawable.remove_background,
        R.drawable.ai_shopping_assistant,
        R.drawable.ic_viraltool_icon
    )

    for (drawableRes in drawables) {
        val drawable = ContextCompat.getDrawable(context, drawableRes)
        assertNotNull("Drawable with ID $drawableRes should exist and be loadable", drawable)
    }
  }

  @Test
  fun `verify HomeScreen renders and tools are displayed`() {
    var navToAiAssistant = false
    var navToThumbnail = false
    var navToSubtitles = false
    var navToVoice = false
    var navToSmartText = false
    var navToShopping = false
    var navToRemoveBg = false

    composeTestRule.setContent {
      MyApplicationTheme {
        HomeScreen(
          onNavigateToAiCreatorAssistant = { navToAiAssistant = true },
          onNavigateToThumbnailPicker = { navToThumbnail = true },
          onNavigateToSubtitlesGenerator = { navToSubtitles = true },
          onNavigateToVoiceCleaner = { navToVoice = true },
          onNavigateToSmartVideoText = { navToSmartText = true },
          onNavigateToShoppingAssistant = { navToShopping = true },
          onNavigateToRemoveBackground = { navToRemoveBg = true }
        )
      }
    }

    // Verify core UI nodes
    composeTestRule.onNodeWithTag("tile_ai_reels").assertIsDisplayed()
    composeTestRule.onNodeWithTag("tile_ai_creator_assistant").assertIsDisplayed()
    composeTestRule.onNodeWithTag("btn_open_shopping_assistant").assertIsDisplayed()

    // Verify all 5 independent Creator Tool cards are displayed
    composeTestRule.onNodeWithTag("tile_thumbnail_picker").assertIsDisplayed()
    composeTestRule.onNodeWithTag("tile_subtitles_generator").assertIsDisplayed()
    composeTestRule.onNodeWithTag("tile_voice_cleaner").assertIsDisplayed()
    composeTestRule.onNodeWithTag("tile_smart_video_text").assertIsDisplayed()
    composeTestRule.onNodeWithTag("tile_remove_background").assertIsDisplayed()

    // Test clicking navigation buttons
    composeTestRule.onNodeWithTag("tile_ai_creator_assistant").performClick()
    assertEquals(true, navToAiAssistant)

    composeTestRule.onNodeWithTag("tile_remove_background").performClick()
    assertEquals(true, navToRemoveBg)

    composeTestRule.onNodeWithTag("tile_thumbnail_picker").performClick()
    assertEquals(true, navToThumbnail)

    composeTestRule.onNodeWithTag("btn_open_shopping_assistant").performClick()
    assertEquals(true, navToShopping)
  }

  @Test
  fun `verify MainAppLayout launches directly to Home when initial shared url provided`() {
    composeTestRule.setContent {
      MyApplicationTheme {
        MainAppLayout(sharedUrl = "https://instagram.com/reel/12345")
      }
    }

    composeTestRule.onNodeWithTag("tile_ai_reels").assertIsDisplayed()
    composeTestRule.onNodeWithTag("tab_home").assertIsDisplayed()
    composeTestRule.onNodeWithTag("tab_creator_hub").assertIsDisplayed()
    composeTestRule.onNodeWithTag("tab_ai_labs").assertIsDisplayed()
  }
}
