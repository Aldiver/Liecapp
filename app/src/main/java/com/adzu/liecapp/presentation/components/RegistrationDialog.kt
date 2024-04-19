package com.adzu.liecapp.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController

@Composable
fun RegistrationDialog(
    dialogState: MutableState<Boolean>,
    onClose: () -> Unit,
    navController: NavController
) {
    if (dialogState.value) {
        AlertDialog(
            onDismissRequest = onClose,
            title = { Text(text = "Registration Successful") },
            text = { Text(text = "Your registration was successful!") },
            confirmButton = {
                Button(onClick = {
                    onClose()
                    navController.navigate("Home")
                }) {
                    Text(text = "OK")
                }
            }
        )
    }
}