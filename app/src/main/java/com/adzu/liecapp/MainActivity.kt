package com.adzu.liecapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.adzu.liecapp.presentation.AppNavigator
import com.adzu.liecapp.presentation.Screens
import com.adzu.liecapp.ui.theme.LiecappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiecappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            BottomNav(navController = navController)
                        }
                    ) {
                        it
                        AppNavigator(
                            navHostController = navController,
                            startDestination = Screens.Home.route
                        )
                    }
                }
            }
        }
    }
}


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
