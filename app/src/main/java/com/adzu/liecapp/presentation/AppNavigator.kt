package com.adzu.liecapp.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LibraryBooks
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.adzu.liecapp.presentation.screens.HomeScreen
import com.adzu.liecapp.presentation.screens.LoginScreen
import com.adzu.liecapp.presentation.screens.ProfileScreen
import com.adzu.liecapp.presentation.screens.RecordsScreen
import com.adzu.liecapp.presentation.screens.ScanningScreen
import com.adzu.liecapp.presentation.screens.VehiclesScreen

@Composable
fun AppNavigator(
    navHostController: NavHostController,
    startDestination: String,
) {

    NavHost(navController = navHostController,
        startDestination = startDestination,
        builder = {
            composable("Login") {
                LoginScreen()
            }
            composable(NavCons.home) {
                HomeScreen()
            }
            composable(NavCons.vehicle) {
                VehiclesScreen()
            }
            composable(NavCons.scan) {
                ScanningScreen(navHostController = navHostController)
            }
            composable(NavCons.record) {
                RecordsScreen()
            }
            composable(NavCons.profile) {
                ProfileScreen()
            }
        })

}

sealed class Screens(val route: String, val imageVector: ImageVector, val label: String) {

    data object Home : Screens(
        route = NavCons.home,
        label = "Home",
        imageVector = Icons.Rounded.Home
    )

    data object Vehicle : Screens(
        route = NavCons.vehicle,
        label = "Vehicles",
        imageVector = Icons.Rounded.DirectionsCar
    )

    data object Scan : Screens(
        route = NavCons.scan,
        label = "Scan",
        imageVector = Icons.Rounded.Camera
    )

    data object Record : Screens(
        route = NavCons.record,
        label = "Record",
        imageVector = Icons.Rounded.LibraryBooks
    )

    data object Profile : Screens(
        route = NavCons.profile,
        label = "Profile",
        imageVector = Icons.Rounded.Person
    )
}

object NavCons {
    const val home = "Home"
    const val vehicle = "Vehicles"
    const val scan = "Scan"
    const val record = "Records"
    const val profile = "Profile"
}