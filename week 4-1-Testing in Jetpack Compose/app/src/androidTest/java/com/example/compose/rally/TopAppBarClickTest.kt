package com.example.compose.rally

import androidx.compose.material.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.rally.ui.components.RallyTopAppBar
import com.example.compose.rally.ui.overview.OverviewBody
import com.example.compose.rally.ui.theme.RallyTheme
import org.junit.Rule
import org.junit.Test

class TopAppBarClickTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun topAppBar_isChangesSelection() {
        val allScreens = RallyScreen.values().toList()
        //개별적인 compose 요소를 테스트 할 수 있음
        var state = mutableStateOf(RallyScreen.Accounts)
        composeTestRule.setContent {
            RallyTheme {
                RallyTopAppBar(
                    allScreens = allScreens,
                    onTabSelected = { state.value = it },
                    currentScreen = RallyScreen.Accounts )
            }
        }

        //
        composeTestRule
            .onNode(
                hasContentDescription(RallyScreen.Bills.name),
                useUnmergedTree = true
            ).performClick()

    }
}