package com.example.shopmobileapplication.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.Notification
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.notifications.NotificationsRepositoryImpl
import com.example.shopmobileapplication.ui.main.components.CustomTopAppBar
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.ralewayRegular
import com.example.shopmobileapplication.ui.theme.ralewaySubregular
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.ui.viewmodel.NotificationsViewModel
import com.example.shopmobileapplication.ui.viewmodel.NotificationsViewModelFactory
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
@Preview
fun NotificationsScreenPreview() {
    NotificationsScreen()
}

@Composable
fun NotificationsScreen(
    notificationsViewModel: NotificationsViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = NotificationsViewModelFactory(
        NotificationsRepositoryImpl(SupabaseClient.client))
    )
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteGreyBackground)
    ) {
        CustomTopAppBar(
            title = stringResource(R.string.notifications),
            onBackButtonClick = { },
            actionIconButton = { }
        )

        LaunchedEffect(Unit) {
            notificationsViewModel.getNotification()
            notificationsViewModel.attachToRealtimeByScope(this)
        }

        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_STOP) {
                    notificationsViewModel.closeConnection(SupabaseClient.client)
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize().background(Color.Transparent).padding(horizontal = 10.dp).padding(top = 10.dp)) {
            items(notificationsViewModel.notifications) { notification: Notification ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth().padding(vertical = 5.dp)
                        .background(Color.Transparent),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(contentColor = Color.White, containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    var showFullText by remember { mutableStateOf(false) }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showFullText = !showFullText
                            }
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(10.dp).padding(bottom = 0.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(26.dp).padding(5.dp).background(Color.Transparent),
                                shape = CircleShape,
                                color = if (notification.readAt != null) Color.LightGray else blueGradientStart
                            ) { }
                            Text(text = LocalDateTime.ofEpochSecond(notification.sendAt, 0, ZoneOffset.UTC).format(
                                DateTimeFormatter.ofPattern("dd.MM.yyyy")), style = ralewaySubregular)
                        }
                        Text(modifier = Modifier.padding(10.dp), text = notification.title, style = ralewaySubtitle)
                        Text(modifier = Modifier.padding(10.dp), text = if (showFullText) notification.message else if (notification.message.length > 125) {
                                notification.message.substring(0, 125).plus("...")
                            } else {
                                notification.message
                            },
                            style = ralewayRegular)

                        notification.readAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
                        LaunchedEffect(Unit) {
                            if (notification.readAt == null) {
                                delay(5000)
                                notificationsViewModel.updateNotification(notification)
                            }
                        }
                    }
                }
            }
        }
    }
}