
package com.adzu.liecapp.presentation.screens

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.adzu.liecapp.R
import com.adzu.liecapp.api.auth.models.Auth
import com.adzu.liecapp.presentation.viewmodels.AuthViewModel
import com.adzu.liecapp.presentation.viewmodels.CoroutinesErrorHandler
import com.adzu.liecapp.presentation.viewmodels.TokenViewModel
import com.adzu.liecapp.utils.ApiResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    val loginStat = remember { mutableStateOf("") }

//    tokenViewModel.token.observe(lifecycleOwner) { token ->
//        if (token != null)
//    }

    viewModel.loginResponse.observe(lifecycleOwner) {
        when(it) {
            is ApiResponse.Failure -> loginStat.value = it.errorMessage
            ApiResponse.Loading -> loginStat.value = "Loading"
            is ApiResponse.Success -> {
                tokenViewModel.saveToken(it.data.token)
                loginStat.value = it.data.token
            }
        }
    }

    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val pxValue = with(LocalDensity.current) { screenHeight.toPx() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Blue, Color.Blue, Color.Black),
                    startY = 0f,
                    endY = pxValue, // Adjust the endY value as needed
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "LIECAP",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp
            )
            Image(
                painter = painterResource(id = R.drawable.adzu_logo),
                contentDescription = null, // Provide a meaningful description if needed
                modifier = Modifier
                    .size(200.dp)
                    .padding(vertical = 16.dp)
                )

            Text(
                text = "University Security Office",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            // Display login status
            Text(
                text = loginStat.value,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )

            // Input fields
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text(
                    text = "Email",
                    color = Color.White
                ) },
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .weight(1f),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.White,
                    focusedBorderColor = Color.White, // Outline color when focused
                    unfocusedBorderColor = Color.White // Outline color when not focused
                ),
                shape = RoundedCornerShape(24.dp)
            )
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text(
                    text = "Password",
                    color = Color.White
                ) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .weight(1f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.White,
                    focusedBorderColor = Color.White, // Outline color when focused
                    unfocusedBorderColor = Color.White // Outline color when not focused
                ),
                shape = RoundedCornerShape(24.dp),
            )

            // Login button
            Spacer(modifier = Modifier.height(200.dp))
            Button(
                onClick = {
                    viewModel.login(
                        Auth("aldever@gmail.com", "helloword"),
                        object : CoroutinesErrorHandler {
                            override fun onError(message: String) {
                                loginStat.value = "Error! $message"
                            }
                        }
                    )
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
                    text = "LOGIN",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
        }
    }
}
