
package com.adzu.liecapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.adzu.liecapp.api.auth.models.Auth
import com.adzu.liecapp.presentation.viewmodels.AuthViewModel
import com.adzu.liecapp.presentation.viewmodels.CoroutinesErrorHandler
import com.adzu.liecapp.presentation.viewmodels.TokenViewModel
import com.adzu.liecapp.utils.ApiResponse

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Display login status
        Text(text = loginStat.value, color = Color.Red, style = MaterialTheme.typography.bodySmall)

        // Input fields
        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text(text = "Email") },
            modifier = Modifier.padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Login button
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
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Login")
        }
    }
}
