package com.example.gymandroid.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymandroid.view.LoginScreen
import com.example.gymandroid.view.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val isLoggedIn = remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn.value) "main" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginClick = { email, password ->
                    // Lógica de autenticación
                    if (email.isNotBlank() && password.isNotBlank()) {
                        isLoggedIn.value = true
                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                isLoading = false
            )
        }

        composable("main") {
            MainScreen()
        }
    }
}