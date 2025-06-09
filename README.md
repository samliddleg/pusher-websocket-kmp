# pusher-websocket-kmp

A Kotlin Multiplatform library for working with [Pusher Channels](https://pusher.com/channels), providing real-time WebSocket support across Android, iOS, and desktop targets.

---

## Features

- Connect to Pusher Channels over WebSocket.
- Subscribe to **public**, **private**, and **presence** channels.
- Bind and handle events.
- Reconnection support.

---

## Installation

Add the library to your Kotlin Multiplatform project.

### Gradle

```kotlin
// build.gradle.kts (project)
repositories {
    mavenCentral()
}

// build.gradle.kts (shared module)
kotlin {
    sourceSets {
        val commonMain.dependencies {
            implementation("uk.co.lidbit:pusher-websocket-kmp:<latest-version>")
        }
    }
}
```

---

## Usage

### Connecting to Pusher

```kotlin
val options = PusherOptions.Builder()
    .setCluster("YOUR_CLUSTER") // or use .setHost(...)
    .setAutoReconnect(true)
    .setUseTls(true)
    .build()

val pusher = PusherClient(
    apiKey = "YOUR_PUSHER_KEY",
    options = options
)

pusher.connect(
    onConnectionStateChange = { current, previous ->
        println("Connection changed from $previous to $current")
    },
    onError = { message, code, exception ->
        println("Connection error: $message ($code), exception: $exception")
    }
)
```

### Subscribing to a Public Channel

```kotlin
pusher.subscribe(
    channel = "my-channel",
    onSuccess = {
        println("Successfully subscribed to my-channel")
    },
    onEvent = { event ->
        println("Received event: ${event.name} with data: ${event.data}")
    },
    "my-event"
)
```

### Subscribing to a Private Channel

```kotlin
// or create your own using the ChannelAuthorizer interface
val channelAuthorizer = HttpChannelAuthorizer(
    endpoint = "https://example.domain/broadcasting/auth",
    headers = {
        mapOf(
            "Authorization" to "Bearer {token}",
        )
    },
)

val options = PusherOptions.Builder()
    .setCluster("YOUR_CLUSTER") // or use .setHost(...)
    .setChannelAuthorizer(channelAuthorizer)
    .build()

val pusher = PusherClient("YOUR_PUSHER_KEY", options)

pusher.connect()

pusher.subscribePrivate(
    channel = "private-my-channel",
    onSuccess = {
        println("Subscribed to private channel")
    },
    onAuthenticationFailure = { message, error ->
        println("Auth failed: $message, exception: $error")
    },
    onEvent = { event ->
        println("Event received: ${event.name}")
    },
    "some-private-event"
)
```

### Subscribing to a Presence Channel

```kotlin
// or create your own using the UserAuthenticator interface
val userAuthenticator = HttpUserAuthenticator(
    endpoint = "https://example.domain/broadcasting/auth",
    headers = {
        mapOf(
            "Authorization" to "Bearer {token}",
        )
    },
)
val options = PusherOptions.Builder()
    .setCluster("YOUR_CLUSTER")
    .setUserAuthenticator(userAuthenticator)
    .build()

val pusher = PusherClient("YOUR_PUSHER_KEY", options)

pusher.connect()

pusher.subscribePresence(
    channel = "presence-my-channel",
    onSuccess = {
        println("Subscribed to presence channel")
    },
    onUsersInformationReceived = { users ->
        // only available for jvm client
        println("Initial users: $users")
    },
    onUserCountChanged = { count ->
        println("User count changed: $count")
    },
    userSubscribed = { user ->
        println("User subscribed: $user")
    },
    userUnsubscribed = { user ->
        println("User unsubscribed: $user")
    },
    onAuthenticationFailure = { message, error ->
        println("Presence auth failed: $message, exception: $error")
    },
    onEvent = { event ->
        println("Presence event: ${event.name}")
    },
    "presence-event"
)
```

### Disconnecting

```kotlin
pusher.disconnect()
```

---

## Supported Platforms

- ✅ Android
- ✅ iOS
- ✅ JVM (Desktop)

---

### Configuration Options

You can customize the `PusherOptions` using the `Builder` class. Below is a list of available configuration options:

| Option                                | JVM | iOS | Description                                                                 |
|---------------------------------------|:---:|:---:|-----------------------------------------------------------------------------|
| `setHost(host: String)`               | ✅  | ✅  | Connect to a custom host instead of using a cluster.                       |
| `setCluster(cluster: String)`         | ✅  | ✅  | Use a predefined Pusher cluster (e.g., `"eu"`). Ignored if `host` is set.  |
| `setProxy(proxy: Proxy)`              | ✅  | ❌  | Set a proxy for WebSocket connections.                                     |
| `setActivityTimeout(timeout: Long)`   | ✅  | ✅  | Time in ms after which a ping is sent if no activity is detected.          |
| `setPongTimeout(timeout: Long)`       | ✅  | ❌  | Time in ms to wait for a pong after sending a ping.                        |
| `setAutoReconnect(autoReconnect: Boolean)` | ✅  | ✅  | Automatically reconnect after disconnection.                          |
| `setMaxReconnectGapInSeconds(seconds: Int)` | ✅  | ❌  | Max delay between reconnection attempts.                             |
| `setMaxReconnectionAttempts(attempts: Int)` | ✅  | ❌  | Max number of reconnection attempts before giving up.               |
| `setUseTls(useTLS: Boolean)`          | ✅  | ✅  | Use secure WebSocket (WSS) connections.                                    |
| `setChannelAuthorizer(authorizer: ChannelAuthorizer)` | ✅  | ✅  | Custom authorizer for **private** channels.                |
| `setUserAuthenticator(authenticator: UserAuthenticator)` | ✅  | ✅  | Custom authenticator for **presence** channels.         |
| `setWsPort(port: Int)`                | ✅  | ✅  | Override default unencrypted WebSocket port (80).                  |
| `setWssPort(port: Int)`               | ✅  | ✅  | Override default encrypted WebSocket port (443).                   |

---

## Roadmap

- [x] Public channel support
- [x] Private channel authentication
- [x] Presence channels
- [ ] Full test coverage
- [ ] Auto reconnect improvements

---

## Contributing

Contributions welcome! Please open an issue or submit a pull request.

---

## License

MIT

---

## Credits

Inspired by:

- [pusher-websocket-java](https://github.com/pusher/pusher-websocket-java)
- [pusher-websocket-swift](https://github.com/pusher/pusher-websocket-swift)