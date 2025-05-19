package uk.co.lidbit.pusher.kmp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PusherClient(apiKey: String, options: PusherOptions) {
    fun connect(
        onConnectionStateChange: (currentState: ConnectionState, previousState: ConnectionState) -> Unit = { _, _ -> },
        onError: (message: String?, code: String?, e: Exception?) -> Unit = { _, _, _ -> },
    )

    fun disconnect()
    fun subscribe(
        channel: String,
        onSuccess: () -> Unit = {},
        onEvent: (PusherEvent) -> Unit = {},
        vararg eventNames: String,
    )

    fun unsubscribe(channel: String)
    fun subscribePrivate(
        channel: String,
        onSuccess: () -> Unit = {},
        onAuthenticationFailure: (message: String?, e: Exception?) -> Unit = { _, _ -> },
        onEvent: (PusherEvent) -> Unit = {},
        vararg eventNames: String,
    )

    fun subscribePresence(
        channel: String,
        onSuccess: () -> Unit = {},
        onUsersInformationReceived: (users: Set<User>) -> Unit = {},
        onUserCountChanged: (count: Long) -> Unit = {},
        userSubscribed: (user: User) -> Unit = {},
        userUnsubscribed: (user: User) -> Unit = {},
        onAuthenticationFailure: (message: String?, e: Exception?) -> Unit = { _, _ -> },
        onEvent: (PusherEvent) -> Unit = {},
        vararg eventNames: String,
    )
}