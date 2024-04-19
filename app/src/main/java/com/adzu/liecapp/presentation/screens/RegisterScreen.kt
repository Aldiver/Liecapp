
package com.adzu.liecapp.presentation.screens

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.navigation.NavController
import com.adzu.liecapp.R
import com.adzu.liecapp.api.auth.models.Auth
import com.adzu.liecapp.api.auth.models.UserRegister
import com.adzu.liecapp.presentation.components.RegistrationDialog
import com.adzu.liecapp.presentation.viewmodels.AuthViewModel
import com.adzu.liecapp.presentation.viewmodels.CoroutinesErrorHandler
import com.adzu.liecapp.presentation.viewmodels.TokenViewModel
import com.adzu.liecapp.utils.ApiResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val nameState = remember { mutableStateOf<String>("") }
    val emailState = remember { mutableStateOf<String>("") }
    val passwordState = remember { mutableStateOf<String>("") }
    val loginStat = remember { mutableStateOf("") }
    val registrationDialogState = remember { mutableStateOf(false) }

    viewModel.registerResponse.observe(lifecycleOwner) {
        when(it) {
            is ApiResponse.Failure -> loginStat.value = it.errorMessage
            ApiResponse.Loading -> loginStat.value = "Loading"
            is ApiResponse.Success -> {
                registrationDialogState.value = true
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
        RegistrationDialog(
            dialogState = registrationDialogState,
            onClose = { registrationDialogState.value = false },
            navController = navController
        )
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
                value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text(
                    text = "Name",
                    color = Color.White
                ) },
                modifier = Modifier
                    .padding(vertical = 8.dp),

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White, // Outline color when focused
                    unfocusedBorderColor = Color.White // Outline color when not focused

                ),
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
            )
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text(
                    text = "Email",
                    color = Color.White
                ) },
                modifier = Modifier
                    .padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White, // Outline color when focused
                    unfocusedBorderColor = Color.White // Outline color when not focused

                ),
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
            )
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text(
                    text = "Password",
                    color = Color.White
                ) },
                modifier = Modifier
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White, // Outline color when focused
                    unfocusedBorderColor = Color.White // Outline color when not focused
                ),
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
            )

            // Login button
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    viewModel.register(
                        UserRegister(nameState.value,emailState.value, passwordState.value),
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
                    text = "Register",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
        }
    }
}
