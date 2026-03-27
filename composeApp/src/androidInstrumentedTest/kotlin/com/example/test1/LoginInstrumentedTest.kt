package com.example.test1

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testLoginSuccess() {
        // Find Username field and type
        composeTestRule.onNodeWithText("Username").performTextInput("testuser")

        // Find Password field and type
        composeTestRule.onNodeWithText("Password").performTextInput("password")

        // Click Login button
        composeTestRule.onNodeWithText("Login").performClick()

        // Verify that we see the welcome message
        // (Adjust the text based on your actual success state)
        composeTestRule.onNodeWithText("Welcome, testuser!", substring = true).assertExists()
    }

    @Test
    fun testToggleToSignUp() {
        // Click the toggle button
        composeTestRule.onNodeWithText("Don't have an account? Sign Up").performClick()

        // Verify "Create Account" header appears
        composeTestRule.onNodeWithText("Create Account").assertExists()
        
        // Verify Email field is now visible
        composeTestRule.onNodeWithText("Email").assertExists()
    }
}
