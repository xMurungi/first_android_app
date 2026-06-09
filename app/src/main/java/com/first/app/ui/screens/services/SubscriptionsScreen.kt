package com.first.app.ui.screens.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.first.app.data.models.SubscriptionDto
import com.first.app.ui.theme.*
import com.first.app.ui.viewmodel.ServicesViewModel
import com.first.app.ui.viewmodel.SubscriptionsUiState
import com.first.app.ui.widgets.EmptyStateScreen
import com.first.app.ui.widgets.ErrorStateScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionsScreen(
    viewModel: ServicesViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.subscriptionsState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadSubscriptions()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title  = { Text("My Subscriptions", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBackIos, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when (val s = state) {
            is SubscriptionsUiState.Loading -> {
                Box(
                    modifier         = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            is SubscriptionsUiState.Error -> {
                Box(modifier = Modifier.padding(padding)) {
                    ErrorStateScreen(
                        message = s.message,
                        onRetry = { viewModel.loadSubscriptions() }
                    )
                }
            }

            is SubscriptionsUiState.Success -> {
                if (s.subscriptions.isEmpty()) {
                    Box(modifier = Modifier.padding(padding)) {
                        EmptyStateScreen(
                            title       = "No active subscriptions",
                            subtitle    = "Browse our services and subscribe to a plan.",
                            actionLabel = "Browse Services",
                            onAction    = onBack
                        )
                    }
                } else {
                    LazyColumn(
                        modifier            = Modifier.padding(padding),
                        contentPadding      = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(s.subscriptions) { sub ->
                            SubscriptionCard(subscription = sub)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionCard(subscription: SubscriptionDto) {
    Card(
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = CardBg),
        border   = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier          = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier         = Modifier
                    .size(44.dp)
                    .background(Accent.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint               = Accent,
                    modifier           = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = subscription.serviceName,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = TextDark
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text     = "KES ${"%.0f".format(subscription.amountPaid)} / month",
                    fontSize = 13.sp,
                    color    = Color.Gray
                )
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Accent.copy(alpha = 0.12f)
            ) {
                Text(
                    text       = "Active",
                    color      = Accent,
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier   = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}