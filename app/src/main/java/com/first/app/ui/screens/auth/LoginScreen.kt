package com.first.app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.first.app.ui.theme.Primary
import com.first.app.ui.theme.TextDark
import com.first.app.ui.viewmodel.AuthUiState
import com.first.app.ui.viewmodel.AuthViewModel
import com.first.app.ui.widgets.AppButton
import com.first.app.ui.widgets.AppTextField
import com.first.app.ui.widgets.ErrorBanner

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var hidePass by remember { mutableStateOf(true) }

    // Email validation
    var emailError    by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    // Navigate on success
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            viewModel.resetState()
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Brand mark
        Surface(
            shape  = RoundedCornerShape(16.dp),
            color  = Primary,
            modifier = Modifier.size(56.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("F", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text       = "Welcome back",
            fontSize   = 26.sp,
            fontWeight = FontWeight.Bold,
            color      = TextDark
        )
        Text(
            text     = "Sign in to your First account",
            fontSize = 15.sp,
            color    = Color.Gray,
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        AppTextField(
            value         = email,
            onValueChange = { email = it; emailError = "" },
            label         = "Email address",
            hint          = "you@example.com",
            isError       = emailError.isNotEmpty(),
            errorMessage  = emailError,
            leadingIcon   = Icons.Outlined.Email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            value                = password,
            onValueChange        = { password = it; passwordError = "" },
            label                = "Password",
            hint                 = "••••••••",
            isError              = passwordError.isNotEmpty(),
            errorMessage         = passwordError,
            leadingIcon          = Icons.Outlined.Lock,
            visualTransformation = if (hidePass) PasswordVisualTransformation()
            else VisualTransformation.None,
            trailingIcon         = {
                IconButton(onClick = { hidePass = !hidePass }) {
                    Icon(
                        imageVector = if (hidePass) Icons.Outlined.Visibility
                        else Icons.Outlined.VisibilityOff,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // API error
        if (uiState is AuthUiState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            ErrorBanner(message = (uiState as AuthUiState.Error).message)
        }

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            label     = "Sign In",
            isLoading = uiState is AuthUiState.Loading,
            onClick   = {
                var valid = true
                if (email.isBlank()) { emailError = "Email is required"; valid = false }
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError = "Enter a valid email"; valid = false
                }
                if (password.isBlank()) { passwordError = "Password is required"; valid = false }
                if (valid) viewModel.login(email.trim(), password)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Don't have an account? ", color = Color.Gray, fontSize = 14.sp)
            TextButton(
                onClick      = onGoToRegister,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text       = "Register",
                    color      = Primary,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}