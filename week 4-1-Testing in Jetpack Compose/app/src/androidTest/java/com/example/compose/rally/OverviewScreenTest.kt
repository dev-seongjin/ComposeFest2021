package com.example.compose.rally

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.rally.ui.components.RallyTopAppBar
import com.example.compose.rally.ui.overview.OverviewBody
import com.example.compose.rally.ui.theme.RallyTheme
import org.junit.Rule
import org.junit.Test

class OverviewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun overviewScreen_alertsDisplayed() {
        val allScreens = RallyScreen.values().toList()
        //개별적인 compose 요소를 테스트 할 수 있음
        composeTestRule.setContent {
            RallyTheme {
                RallyTopAppBar(
                    allScreens = allScreens,
                    onTabSelected = { },
                    currentScreen = RallyScreen.Accounts )
            }
        }

        //UI 트리 (Semantic Tree) 출력 (디버깅)
        //useUnmergedTree : 각 노드 하위의 요소들도 출력 (예, 버튼의 텍스트)
        //useUnmergedTree를 통해 위 구성 요소를 쿼리 할 수 있다
        //composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExist")
        composeTestRule
            .onNode(
                hasText(RallyScreen.Accounts.name.uppercase()) and
                        hasParent(
                            hasContentDescription(RallyScreen.Accounts.name)
                        ),
                useUnmergedTree = true
            )
            .assertExists()
    }
}