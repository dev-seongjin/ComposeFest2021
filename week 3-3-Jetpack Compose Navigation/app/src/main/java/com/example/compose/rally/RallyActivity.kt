/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.rally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.rally.data.UserData
import com.example.compose.rally.data.UserData.accounts
import com.example.compose.rally.data.UserData.bills
import com.example.compose.rally.ui.accounts.AccountsBody
import com.example.compose.rally.ui.accounts.SingleAccountBody
import com.example.compose.rally.ui.bills.BillsBody
import com.example.compose.rally.ui.components.RallyTabRow
import com.example.compose.rally.ui.overview.OverviewBody
import com.example.compose.rally.ui.theme.RallyTheme

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp()
        }
    }
}

@Composable
fun RallyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = RallyScreen.Overview.name,
        modifier = modifier
    ) {

        val accountsName = RallyScreen.Accounts.name

        //개별 Account 항목으로 이동할 수 있음
        composable(
            //Account/이름 으로 경로를 설정함
            route = "$accountsName/{name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                }
            ),
            //deeplink 대응
            deepLinks = listOf(navDeepLink {
                uriPattern = "rally://$accountsName/{name}"
            })
        ) { entry ->
            //argument로 넘어온 이름을 받아 개별 Account 화면 표시
            val accountName = entry.arguments?.getString("name")
            val account = UserData.getAccount(accountName)
            SingleAccountBody(account = account)
        }

        //start로부터 도착할 수 있는 지점
        composable(RallyScreen.Overview.name) {
            OverviewBody(
                onClickSeeAllAccounts = { navController.navigate(RallyScreen.Accounts.name) },
                onClickSeeAllBills = { navController.navigate(RallyScreen.Bills.name) },
                onAccountClick = { name ->
                    navigateToSingleAccount(navController, name)
                }
            )
        }
        composable(RallyScreen.Accounts.name) {
            AccountsBody(accounts = accounts) { name ->
                navigateToSingleAccount(
                    navController = navController,
                    accountName = name
                )
            }
        }
        composable(RallyScreen.Bills.name) {
            BillsBody(bills = bills)
            Text(text = RallyScreen.Overview.name)
        }
    }
}

@Composable
fun RallyApp() {
    RallyTheme {
        val allScreens = RallyScreen.values().toList()
        val navController = rememberNavController()
        //백스택 엔트리를 상태(State)로 제공
        val backStackEntry = navController.currentBackStackEntryAsState()
        //백스택에 있는 현재의 스크린을 쿼리
        //백스택의 스크린 = 현재 스크린 = 백스택의 스크린이 변경될때 마다 currentScreen도 변경
        var currentScreen = RallyScreen.fromRoute(
            backStackEntry.value?.destination?.route
        )

        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = allScreens,
                    onTabSelected = { screen ->
                        navController.navigate(screen.name)
                    },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            RallyNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

private fun navigateToSingleAccount(
    navController: NavController,
    accountName: String
) {
    navController.navigate("${RallyScreen.Accounts.name}/$accountName")
}
