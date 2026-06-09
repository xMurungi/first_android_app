package com.first.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.first.app.ui.screens.auth.LoginScreen
import com.first.app.ui.screens.auth.RegisterScreen
import com.first.app.ui.screens.services.ServicesScreen
import com.first.app.ui.screens.services.SubscriptionsScreen
import com.first.app.ui.screens.SplashScreen
import com.first.app.ui.viewmodel.AuthViewModel
import com.first.app.ui.viewmodel.ServicesViewModel

object Routes {
    const val SPLASH        = "splash"
    const val LOGIN         = "login"
    const val REGISTER      = "register"
    const val SERVICES      = "services"
    const val SUBSCRIPTIONS = "subscriptions"
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    val authViewModel: AuthViewModel     = viewModel()
    val servicesViewModel: ServicesViewModel = viewModel()

    NavHost(
        navController   = navController,
        startDestination = Routes.SPLASH
    ) {

        composable(Routes.SPLASH) {
            SplashScreen(
                onReady = { isLoggedIn ->
                    if (isLoggedIn) {
                        navController.navigate(Routes.SERVICES) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel       = authViewModel,
                onLoginSuccess  = {
                    navController.navigate(Routes.SERVICES) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoToRegister  = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel          = authViewModel,
                onRegisterSuccess  = {
                    navController.popBackStack(Routes.LOGIN, inclusive = false)
                },
                onGoToLogin        = { navController.popBackStack() }
            )
        }

        composable(Routes.SERVICES) {
            ServicesScreen(
                viewModel              = servicesViewModel,
                authViewModel          = authViewModel,
                onGoToSubscriptions    = { navController.navigate(Routes.SUBSCRIPTIONS) },
                onLogout               = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SERVICES) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.SUBSCRIPTIONS) {
            SubscriptionsScreen(
                viewModel = servicesViewModel,
                onBack    = { navController.popBackStack() }
            )
        }
    }
}