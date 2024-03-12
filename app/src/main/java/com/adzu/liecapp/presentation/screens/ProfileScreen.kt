package com.adzu.liecapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.adzu.liecapp.api.auth.models.Auth
import com.adzu.liecapp.api.auth.models.UserInfo
import com.adzu.liecapp.api.auth.models.UserInfoResponse
import com.adzu.liecapp.api.main.models.VehicleInfo
import com.adzu.liecapp.api.main.repository.MainRepository
import com.adzu.liecapp.presentation.viewmodels.AuthViewModel
import com.adzu.liecapp.presentation.viewmodels.CoroutinesErrorHandler
import com.adzu.liecapp.presentation.viewmodels.MainViewModel
import com.adzu.liecapp.presentation.viewmodels.TokenViewModel
import com.adzu.liecapp.utils.ApiResponse

@Composable
fun ProfileScreen(
    viewModel : MainViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel(),
    navController: NavController
) {

    val configuration = LocalConfiguration.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val screenHeight = configuration.screenHeightDp.dp
    val pxValue = with(LocalDensity.current) { screenHeight.toPx() }

    val authDetail by
    viewModel.userInfoResponse.observeAsState(ApiResponse.Loading)

    val status = remember { mutableStateOf("No Error found") }


    tokenViewModel.token.observe(lifecycleOwner) { token ->
        if (token == null) {
            navController.navigate("login")
        }
    }

    // Fetch list of vehicles when the screen is first shown
    LaunchedEffect(key1 = true) {
        viewModel.getUserInfo(object: CoroutinesErrorHandler {
            override fun onError(message: String) {
                status.value = "Error! $message"
            }
        })
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Blue, Color.Blue, Color.Black),
                startY = 0f,
                endY = pxValue, // Adjust the endY value as needed
            )
        )
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "User Profile",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge
                )

                UserInfoView(authDetail)
                Button(
                    onClick = {
                        tokenViewModel.deleteToken()
                    },
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green // Set button background color to green
                    )
                ) {
                    Text(
                        text = "LOGOUT",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }

            }
        }
    }
}

@Composable
fun UserInfoView(authDetail: ApiResponse<UserInfoResponse>) {
    when (authDetail) {
        is ApiResponse.Failure -> {
            Text(
                text = "Error retrieving data ${authDetail.code}",
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
        ApiResponse.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.width(24.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
        is ApiResponse.Success -> {
            val userInformation = authDetail.data.userInfo
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(text = "ID: ${userInformation.id}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Name: ${userInformation.name}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Email: ${userInformation.email}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
