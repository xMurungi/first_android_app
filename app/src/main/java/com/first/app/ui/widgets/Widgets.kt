package com.first.app.ui.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.first.app.ui.theme.*

// ── Text Field ────────────────────────────────────────────────

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    hint: String = "",
    isError: Boolean = false,
    errorMessage: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: androidx.compose.foundation.text.KeyboardOptions =
        androidx.compose.foundation.text.KeyboardOptions.Default,
    singleLine: Boolean = true
) {
    Column(modifier = modifier) {
        Text(
            text       = label,
            fontSize   = 13.sp,
            fontWeight = FontWeight.Medium,
            color      = TextDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value                = value,
            onValueChange        = onValueChange,
            placeholder          = { Text(hint, color = Color.Gray.copy(alpha = 0.5f)) },
            isError              = isError,
            leadingIcon          = leadingIcon?.let { { Icon(it, contentDescription = null,
                tint = Color.Gray, modifier = Modifier.size(20.dp)) } },
            trailingIcon         = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions      = keyboardOptions,
            singleLine           = singleLine,
            shape                = RoundedCornerShape(12.dp),
            colors               = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = Primary,
                unfocusedBorderColor = BorderColor,
                errorBorderColor     = ErrorRed,
                focusedContainerColor   = FieldBg,
                unfocusedContainerColor = FieldBg,
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text     = errorMessage,
                color    = ErrorRed,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

// ── Primary Button ────────────────────────────────────────────

@Composable
fun AppButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    Button(
        onClick  = onClick,
        enabled  = enabled && !isLoading,
        shape    = RoundedCornerShape(12.dp),
        colors   = ButtonDefaults.buttonColors(containerColor = Primary),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color       = Color.White,
                strokeWidth = 2.dp,
                modifier    = Modifier.size(22.dp)
            )
        } else {
            Text(label, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// ── Error Banner ──────────────────────────────────────────────

@Composable
fun ErrorBanner(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width  = 1.dp,
                color  = ErrorRed.copy(alpha = 0.3f),
                shape  = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text     = message,
            color    = ErrorRed,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

// ── Full Screen Error State ───────────────────────────────────

@Composable
fun ErrorStateScreen(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier            = Modifier.padding(32.dp)
        ) {
            Text(
                text       = "Something went wrong",
                fontSize   = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color      = TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text      = message,
                fontSize  = 14.sp,
                color     = Color.Gray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                shape   = RoundedCornerShape(12.dp),
                colors  = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("Try Again")
            }
        }
    }
}

// ── Empty State ───────────────────────────────────────────────

@Composable
fun EmptyStateScreen(
    title: String,
    subtitle: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier            = Modifier.padding(32.dp)
        ) {
            Text(
                text       = title,
                fontSize   = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color      = TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text      = subtitle,
                fontSize  = 14.sp,
                color     = Color.Gray,
                textAlign = TextAlign.Center
            )
            if (actionLabel != null && onAction != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onAction,
                    shape   = RoundedCornerShape(12.dp),
                    colors  = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text(actionLabel)
                }
            }
        }
    }
}