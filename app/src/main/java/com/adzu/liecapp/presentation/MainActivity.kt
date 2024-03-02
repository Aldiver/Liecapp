package com.adzu.liecapp.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.adzu.liecapp.presentation.AppNavigator
import com.adzu.liecapp.presentation.Screens
import com.adzu.liecapp.presentation.components.BottomNav
import com.adzu.liecapp.presentation.screens.LoginScreen
import com.adzu.liecapp.presentation.screens.NoPermissionScreen
import com.adzu.liecapp.presentation.viewmodels.TokenViewModel
import com.adzu.liecapp.ui.theme.LiecappTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)

            LiecappTheme {
                val hasPermission = cameraPermissionState.status.isGranted
                var onRequestPermission = cameraPermissionState::launchPermissionRequest
                var loggedIn by remember { mutableStateOf(false) }

                // Observe the token to determine if the user is logged in
                val tokenViewModel: TokenViewModel = hiltViewModel()
                val token by tokenViewModel.token.observeAsState()

                // Check if the token is not null to indicate the user is logged in
                if (true) {
                    // User is logged in, show the main screen with bottom navigation
                    Scaffold(
                        bottomBar = {
                            BottomNav(navController = navController)
                        }
                    ) { innerPadding ->

                        val modifier = Modifier.padding(innerPadding)

                        Column(modifier = modifier) {
                            AppNavigator(
                                navHostController = navController,
                                startDestination = Screens.Home.route
                            )
                        }
                    }
                } else {
                    // User is not logged in, show the login screen
                    if(hasPermission){
                        LoginScreen()
                    } else {
                        NoPermissionScreen(onRequestPermission)
                    }
                }
            }
        }
    }
}
