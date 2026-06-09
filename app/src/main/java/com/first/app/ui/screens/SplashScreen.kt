package com.first.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.first.app.ui.theme.Primary
import com.first.app.ui.viewmodel.AuthViewModel

@Composable
fun SplashScreen(
    onReady: (Boolean) -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        authViewModel.checkLoginState { isLoggedIn ->
            onReady(isLoggedIn)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color  = Color.White.copy(alpha = 0.15f),
                        shape  = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Filled.Bolt,
                    contentDescription = "First Logo",
                    tint               = Color.White,
                    modifier           = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text       = "First",
                color      = Color.White,
                fontSize   = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text     = "Your subscription hub",
                color    = Color.White.copy(alpha = 0.75f),
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            CircularProgressIndicator(
                color       = Color.White,
                strokeWidth = 2.dp,
                modifier    = Modifier.size(28.dp)
            )
        }
    }
}