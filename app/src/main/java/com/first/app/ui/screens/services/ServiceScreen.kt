package com.first.app.ui.screens.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.first.app.data.models.ServiceDto
import com.first.app.ui.theme.*
import com.first.app.ui.viewmodel.AuthViewModel
import com.first.app.ui.viewmodel.ServicesUiState
import com.first.app.ui.viewmodel.ServicesViewModel
import com.first.app.ui.viewmodel.SubscribeUiState
import com.first.app.ui.widgets.EmptyStateScreen
import com.first.app.ui.widgets.ErrorStateScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    viewModel: ServicesViewModel,
    authViewModel: AuthViewModel,
    onGoToSubscriptions: () -> Unit,
    onLogout: () -> Unit
) {
    val servicesState  by viewModel.servicesState.collectAsStateWithLifecycle()
    val subscribeState by viewModel.subscribeState.collectAsStateWithLifecycle()
    val loadingId      by viewModel.loadingServiceId.collectAsStateWithLifecycle()
    val snackbarHost   = remember { SnackbarHostState() }

    // Show snackbar on subscribe result
    LaunchedEffect(subscribeState) {
        when (subscribeState) {
            is SubscribeUiState.Success -> {
                snackbarHost.showSnackbar((subscribeState as SubscribeUiState.Success).message)
                viewModel.resetSubscribeState()
            }
            is SubscribeUiState.Error -> {
                snackbarHost.showSnackbar((subscribeState as SubscribeUiState.Error).message)
                viewModel.resetSubscribeState()
            }
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHost) },
        topBar = {
            TopAppBar(
                title  = { Text("Services", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onGoToSubscriptions) {
                        Icon(Icons.Outlined.Bookmark, contentDescription = "My Plans", tint = Primary)
                    }
                    IconButton(onClick = {
                        authViewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Outlined.Logout, contentDescription = "Logout", tint = Primary)
                    }
                }
            )
        }
    ) { padding ->
        when (val state = servicesState) {
            is ServicesUiState.Loading -> {
                Box(
                    modifier        = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            is ServicesUiState.Error -> {
                Box(modifier = Modifier.padding(padding)) {
                    ErrorStateScreen(
                        message = state.message,
                        onRetry = { viewModel.loadServices() }
                    )
                }
            }

            is ServicesUiState.Success -> {
                if (state.services.isEmpty()) {
                    Box(modifier = Modifier.padding(padding)) {
                        EmptyStateScreen(
                            title    = "No services yet",
                            subtitle = "Check back soon for available subscription plans."
                        )
                    }
                } else {
                    val discounted = state.services.filter { it.isDiscounted }
                    val regular    = state.services.filter { !it.isDiscounted }

                    LazyColumn(
                        modifier            = Modifier.padding(padding),
                        contentPadding      = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (discounted.isNotEmpty()) {
                            item {
                                Text(
                                    text       = "🔥 Special Offers",
                                    fontSize   = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color      = Discount
                                )
                            }
                            items(discounted) { service ->
                                ServiceCard(
                                    service       = service,
                                    onSubscribe   = { viewModel.subscribe(service) },
                                    isSubscribing = loadingId == service.id
                                )
                            }
                            item { Spacer(modifier = Modifier.height(8.dp)) }
                        }

                        if (regular.isNotEmpty()) {
                            item {
                                Text(
                                    text       = "All Services",
                                    fontSize   = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color      = TextDark
                                )
                            }
                            items(regular) { service ->
                                ServiceCard(
                                    service       = service,
                                    onSubscribe   = { viewModel.subscribe(service) },
                                    isSubscribing = loadingId == service.id
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceCard(
    service: ServiceDto,
    onSubscribe: () -> Unit,
    isSubscribing: Boolean
) {
    Card(
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = CardBg),
        border   = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier         = Modifier
                            .size(44.dp)
                            .background(Primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector        = Icons.Outlined.Subscriptions,
                            contentDescription = null,
                            tint               = Primary,
                            modifier           = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text       = service.serviceName,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextDark
                    )
                }

                // Discount badge
                if (service.isDiscounted) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Discount.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text     = "${service.discountPercent.toInt()}% OFF",
                            color    = Discount,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = BorderColor)
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column {
                    if (service.isDiscounted) {
                        Text(
                            text     = "KES ${service.pricing}",
                            fontSize = 12.sp,
                            color    = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text       = "KES ${"%.0f".format(service.displayPrice)}",
                            fontSize   = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color      = if (service.isDiscounted) Discount else TextDark
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text     = "/ mo",
                            fontSize = 12.sp,
                            color    = Color.Gray,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }

                Button(
                    onClick  = onSubscribe,
                    enabled  = !isSubscribing,
                    shape    = RoundedCornerShape(10.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.height(40.dp)
                ) {
                    if (isSubscribing) {
                        CircularProgressIndicator(
                            color       = Color.White,
                            strokeWidth = 2.dp,
                            modifier    = Modifier.size(18.dp)
                        )
                    } else {
                        Text("Subscribe", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}