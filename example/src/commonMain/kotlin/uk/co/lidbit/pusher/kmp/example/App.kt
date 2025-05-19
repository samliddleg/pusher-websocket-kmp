package uk.co.lidbit.pusher.kmp.example

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uk.co.lidbit.pusher.kmp.ConnectionState
import uk.co.lidbit.pusher.kmp.PusherClient
import uk.co.lidbit.pusher.kmp.PusherOptions

@Composable
@Preview
fun App() {
    val pusherClient = remember {
        PusherClient(
            apiKey = "{API_KEY}",
            options = PusherOptions.Builder()
                .setCluster("eu")
                .build()
        )
    }
    var state by remember { mutableStateOf(ConnectionState.DISCONNECTED) }
    var connectionError by remember { mutableStateOf("") }
    var subscriptionState by remember { mutableStateOf("Unsubscribed") }
    var latestChannelData by remember { mutableStateOf("") }

    MaterialTheme {
        Scaffold { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Text("State: ${state.name}")
                if (connectionError.isNotBlank()) {
                    Text("Connection Error: $connectionError")
                }
                Button(onClick = {
                    pusherClient.connect(
                        onConnectionStateChange = { currentState, previousState ->
                            state = currentState
                        },
                        onError = { message, code, e ->
                            connectionError = message ?: ""
                        }
                    )
                }) {
                    Text("Connect")
                }
                Button(onClick = { pusherClient.disconnect() }) {
                    Text("Disconnect")
                }
                Text("Subscription state: $subscriptionState")
                Button(onClick = {
                    pusherClient.subscribe(
                        channel = "test-channel",
                        onSuccess = {
                            subscriptionState = "Subscribed"
                        },
                        onEvent = {
                            latestChannelData = it.data.orEmpty()
                        },
                        "test.event"
                    )
                }) {
                    Text("Subscribe")
                }
                Button(onClick = {
                    pusherClient.subscribePrivate(
                        channel = "private-test.channel",
                        onSuccess = {
                            subscriptionState = "Subscribed"
                        },
                        onAuthenticationFailure = { message, e ->
                            subscriptionState = "Authentication failure: $message"
                        },
                        onEvent = {
                            latestChannelData = it.data.orEmpty()
                        },
                        "test.event",
                    )
                }) {
                    Text("Subscribe (private)")
                }
                Button(onClick = {
                    pusherClient.unsubscribe("test-channel")
                    subscriptionState = "Unsubscribed"
                }) {
                    Text("Unsubscribe")
                }
                Spacer(Modifier.height(16.dp))
                Text("Latest channel data:")
                Text(latestChannelData)
            }
        }
    }
}