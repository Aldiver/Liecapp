package com.adzu.liecapp.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.adzu.liecapp.presentation.Screens

@Composable
fun BottomNav(
    navController: NavHostController,
) {
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    val list = listOf(
        Screens.Home,
        Screens.Vehicle,
        Screens.Scan,
        Screens.Record,
        Screens.Profile,
    )
    NavigationBar(

    ) {
        list.forEachIndexed { index, screens ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    navController.navigate(screens.route)
                    selectedIndex = index
                },
                icon = {
                    Icon(imageVector = screens.imageVector, contentDescription = "")
                },
                label = {
                    Text(text = screens.label)

                },
                alwaysShowLabel = false

            )
        }
    }
}