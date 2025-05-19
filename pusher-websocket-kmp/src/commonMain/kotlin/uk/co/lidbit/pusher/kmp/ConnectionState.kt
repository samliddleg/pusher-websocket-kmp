package uk.co.lidbit.pusher.kmp

enum class ConnectionState {
    CONNECTING,
    CONNECTED,
    DISCONNECTING,
    DISCONNECTED,
    RECONNECTING,
    ALL,
}