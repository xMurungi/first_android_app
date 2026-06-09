package com.first.app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.*
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
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onGoToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var fullName    by remember { mutableStateOf("") }
    var email       by remember { mutableStateOf("") }
    var msisdn      by remember { mutableStateOf("") }
    var credentials by remember { mutableStateOf("") }
    var confirm     by remember { mutableStateOf("") }
    var hidePass    by remember { mutableStateOf(true) }
    var hideConfirm by remember { mutableStateOf(true) }

    var fullNameError    by remember { mutableStateOf("") }
    var emailError       by remember { mutableStateOf("") }
    var msisdnError      by remember { mutableStateOf("") }
    var credentialsError by remember { mutableStateOf("") }
    var confirmError     by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            viewModel.resetState()
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        IconButton(onClick = onGoToLogin) {
            Icon(Icons.Filled.ArrowBackIos, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text       = "Create account",
            fontSize   = 26.sp,
            fontWeight = FontWeight.Bold,
            color      = TextDark
        )
        Text(
            text     = "Join First and manage your subscriptions",
            fontSize = 15.sp,
            color    = Color.Gray,
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        AppTextField(
            value         = fullName,
            onValueChange = { fullName = it; fullNameError = "" },
            label         = "Full name",
            hint          = "John Doe",
            isError       = fullNameError.isNotEmpty(),
            errorMessage  = fullNameError,
            leadingIcon   = Icons.Outlined.Person
        )

        Spacer(modifier = Modifier.height(14.dp))

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

        Spacer(modifier = Modifier.height(14.dp))

        AppTextField(
            value         = msisdn,
            onValueChange = { msisdn = it; msisdnError = "" },
            label         = "Phone number",
            hint          = "+254 7XX XXX XXX",
            isError       = msisdnError.isNotEmpty(),
            errorMessage  = msisdnError,
            leadingIcon   = Icons.Outlined.Phone,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(14.dp))

        AppTextField(
            value                = credentials,
            onValueChange        = { credentials = it; credentialsError = "" },
            label                = "Password",
            hint                 = "••••••••",
            isError              = credentialsError.isNotEmpty(),
            errorMessage         = credentialsError,
            leadingIcon          = Icons.Outlined.Lock,
            visualTransformation = if (hidePass) PasswordVisualTransformation()
            else VisualTransformation.None,
            trailingIcon         = {
                IconButton(onClick = { hidePass = !hidePass }) {
                    Icon(
                        imageVector        = if (hidePass) Icons.Outlined.Visibility
                        else Icons.Outlined.VisibilityOff,
                        contentDescription = null,
                        tint               = Color.Gray
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(14.dp))

        AppTextField(
            value                = confirm,
            onValueChange        = { confirm = it; confirmError = "" },
            label                = "Confirm password",
            hint                 = "••••••••",
            isError              = confirmError.isNotEmpty(),
            errorMessage         = confirmError,
            leadingIcon          = Icons.Outlined.Lock,
            visualTransformation = if (hideConfirm) PasswordVisualTransformation()
            else VisualTransformation.None,
            trailingIcon         = {
                IconButton(onClick = { hideConfirm = !hideConfirm }) {
                    Icon(
                        imageVector        = if (hideConfirm) Icons.Outlined.Visibility
                        else Icons.Outlined.VisibilityOff,
                        contentDescription = null,
                        tint               = Color.Gray
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState is AuthUiState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            ErrorBanner(message = (uiState as AuthUiState.Error).message)
        }

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            label     = "Create Account",
            isLoading = uiState is AuthUiState.Loading,
            onClick   = {
                var valid = true
                if (fullName.isBlank()) {
                    fullNameError = "Full name is required"; valid = false
                }
                if (email.isBlank()) {
                    emailError = "Email is required"; valid = false
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError = "Enter a valid email"; valid = false
                }
                if (msisdn.isBlank()) {
                    msisdnError = "Phone number is required"; valid = false
                } else if (msisdn.replace(Regex("[\\s+\\-()]"), "").length < 9) {
                    msisdnError = "Enter a valid phone number"; valid = false
                }
                if (credentials.isBlank()) {
                    credentialsError = "Password is required"; valid = false
                } else if (credentials.length < 8) {
                    credentialsError = "Password must be at least 8 characters"; valid = false
                }
                if (confirm != credentials) {
                    confirmError = "Passwords do not match"; valid = false
                }
                if (valid) viewModel.register(
                    fullName    = fullName.trim(),
                    email       = email.trim(),
                    msisdn      = msisdn.trim(),
                    credentials = credentials
                )
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Already have an account? ", color = Color.Gray, fontSize = 14.sp)
            TextButton(
                onClick        = onGoToLogin,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text       = "Sign in",
                    color      = Primary,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}