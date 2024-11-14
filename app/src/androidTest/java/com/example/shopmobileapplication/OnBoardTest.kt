package com.example.shopmobileapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.shopmobileapplication.ui.Layouts
import com.example.shopmobileapplication.ui.auth.SignIn
import com.example.shopmobileapplication.ui.greetings.Onboard
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OnBoardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun testUseAppContext() {
        assertEquals("com.example.shopmobileapplication", appContext.packageName)
    }

    data class ScreenContent(
        val text: String,
        val imageId: Int,
        val buttonText: String
    )

    @Test
    fun testOnBoardQueue() {
        var testScreens: List<ScreenContent> = emptyList()
        composeTestRule.setContent {
            val mainNavController = rememberNavController()
            NavHost(
                modifier = Modifier
                    .background(whiteGreyBackground)
                    .fillMaxSize(),
                navController = mainNavController,
                startDestination = Layouts.ONBOARD_LAYOUT
            ) {
                composable(Layouts.ONBOARD_LAYOUT) {
                    Onboard {
                        mainNavController.navigate(Layouts.SIGN_IN_LAYOUT)
                    }
                }
                composable(Layouts.SIGN_IN_LAYOUT) {
                    SignIn(navController = mainNavController)
                }
            }
            testScreens = listOf(
                ScreenContent(stringResource(R.string.welcome), R.drawable.onboard_1_all, stringResource(R.string.next)),
                ScreenContent(stringResource(R.string.lets_start_trip), R.drawable.onboard_2_all, stringResource(R.string.next)),
                ScreenContent(stringResource(R.string.you_have_force), R.drawable.onboard_3_all, stringResource(R.string.lets_start))
            )
        }

        testScreens.forEachIndexed { index: Int,  content: ScreenContent ->
            composeTestRule.onNodeWithText(content.text).assertExists()
            composeTestRule.onNodeWithTag(content.imageId.toString()).assertExists()
            val button = composeTestRule.onNode(
        hasClickAction()
                and
                hasText(content.buttonText)
            )
            button.assertExists()
            button.performClick()
        }

        composeTestRule.waitForIdle()
        composeTestRule.waitUntil(2000L) {
            true
        }

//        composeTestRule.onNodeWithText(appContext.getString(R.string.enter) + "efwr")

        composeTestRule.onNode(
            hasClickAction()
            and
            hasText(appContext.getString(R.string.enter) + "efwr")
        )
    }
}