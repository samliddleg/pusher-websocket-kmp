package uk.co.lidbit.pusher.kmp

import com.pusher.client.Pusher
import com.pusher.client.channel.ChannelEventListener
import com.pusher.client.channel.PresenceChannelEventListener
import com.pusher.client.channel.PrivateChannelEventListener
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionStateChange
import java.lang.Exception

actual class PusherClient actual constructor(apiKey: String, options: PusherOptions) {

    private val pusher = Pusher(apiKey, options.toAbstracted())

    actual fun connect(
        onConnectionStateChange: (currentState: ConnectionState, previousState: ConnectionState) -> Unit,
        onError: (message: String?, code: String?, e: kotlin.Exception?) -> Unit,
    ) {
        pusher.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange?) {
                change?.let {
                    onConnectionStateChange(
                        it.currentState.toAbstracted(),
                        it.previousState.toAbstracted(),
                    )
                }
            }

            override fun onError(
                message: String?,
                code: String?,
                e: Exception?
            ) {
                onError(message, code, e)
            }
        })
    }

    actual fun disconnect() {
        pusher.disconnect()
    }

    actual fun subscribe(
        channel: String,
        onSuccess: () -> Unit,
        onEvent: (PusherEvent) -> Unit,
        vararg eventNames: String,
    ) {
        pusher.subscribe(
            channel,
            object : ChannelEventListener {
                override fun onSubscriptionSucceeded(channelName: String?) {
                    onSuccess()
                }

                override fun onEvent(event: com.pusher.client.channel.PusherEvent?) {
                    event?.let {
                        onEvent(it.toAbstracted())
                    }
                }
            },
            *eventNames,
        )
    }

    actual fun unsubscribe(channel: String) {
        pusher.unsubscribe(channel)
    }

    actual fun subscribePrivate(
        channel: String,
        onSuccess: () -> Unit,
        onAuthenticationFailure: (message: String?, e: kotlin.Exception?) -> Unit,
        onEvent: (PusherEvent) -> Unit,
        vararg eventNames: String,
    ) {
        pusher.subscribePrivate(
            channel,
            object : PrivateChannelEventListener {
                override fun onAuthenticationFailure(
                    message: String?,
                    e: Exception?
                ) {
                    onAuthenticationFailure(message, e)
                }

                override fun onSubscriptionSucceeded(channelName: String?) {
                    onSuccess()
                }

                override fun onEvent(event: com.pusher.client.channel.PusherEvent?) {
                    event?.let {
                        onEvent(it.toAbstracted())
                    }
                }
            },
            *eventNames,
        )
    }

    actual fun subscribePresence(
        channel: String,
        onSuccess: () -> Unit,
        onUsersInformationReceived: (users: Set<User>) -> Unit,
        onUserCountChanged: (count: Long) -> Unit,
        userSubscribed: (user: User) -> Unit,
        userUnsubscribed: (user: User) -> Unit,
        onAuthenticationFailure: (message: String?, e: kotlin.Exception?) -> Unit,
        onEvent: (PusherEvent) -> Unit,
        vararg eventNames: String,
    ) {
        pusher.subscribePresence(
            channel,
            object : PresenceChannelEventListener {
                override fun onUsersInformationReceived(
                    channelName: String?,
                    users: Set<com.pusher.client.channel.User?>?
                ) {
                    users?.size?.let { onUserCountChanged(it.toLong()) }
                    users?.mapNotNull { it?.toAbstracted() }?.toSet()
                        ?.let { onUsersInformationReceived(it) }
                }

                override fun userSubscribed(
                    channelName: String?,
                    user: com.pusher.client.channel.User?
                ) {
                    user?.let { userSubscribed(it.toAbstracted()) }
                }

                override fun userUnsubscribed(
                    channelName: String?,
                    user: com.pusher.client.channel.User?
                ) {
                    user?.let { userUnsubscribed(it.toAbstracted()) }
                }

                override fun onAuthenticationFailure(
                    message: String?,
                    e: Exception?
                ) {
                    onAuthenticationFailure(message, e)
                }

                override fun onSubscriptionSucceeded(channelName: String?) {
                    onSuccess()
                }

                override fun onEvent(event: com.pusher.client.channel.PusherEvent?) {
                    event?.let {
                        onEvent(it.toAbstracted())
                    }
                }
            },
            *eventNames,
        )
    }

}