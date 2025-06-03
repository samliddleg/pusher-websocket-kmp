package uk.co.lidbit.pusher.kmp

import cocoapods.PusherSwift.Pusher
import cocoapods.PusherSwift.PusherDelegateProtocol
import cocoapods.PusherSwift.PusherError
import cocoapods.PusherSwift.create
import cocoapods.PusherSwift.PusherAuth
import cocoapods.PusherSwift.subscribeWithChannelName
import cocoapods.PusherSwift.subscribeToPresenceChannelWithChannelName
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSError
import platform.Foundation.NSURLResponse
import platform.darwin.NSObject


@OptIn(ExperimentalForeignApi::class)
actual class PusherClient actual constructor(apiKey: String, options: PusherOptions) {

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private val pusher = Pusher.create(apiKey, options.toSwift())

    private var onConnectionChange: ((currentState: ConnectionState, previousState: ConnectionState) -> Unit)? =
        null

    private var onConnectionError: ((String) -> Unit)? = null

    private val eventCallbacks = hashMapOf<String, (PusherEvent) -> Unit>()
    private val subscribeSuccessCallbacks = hashMapOf<String, () -> Unit>()
    private val onAuthenticationFailureCallbacks =
        hashMapOf<String, (message: String?, e: Exception?) -> Unit>()

    private val delegate = object : NSObject(), PusherDelegateProtocol {
        override fun changedConnectionStateFrom(
            old: cocoapods.PusherSwift.ConnectionState,
            to: cocoapods.PusherSwift.ConnectionState
        ) {
            onConnectionChange?.invoke(to.toAbstracted(), old.toAbstracted())
        }

        override fun debugLogWithMessage(message: String) {}

        override fun failedToDecryptEventWithEventName(
            eventName: String,
            channelName: String,
            data: String?
        ) {
        }

        override fun failedToSubscribeToChannelWithName(
            name: String,
            response: NSURLResponse?,
            data: String?,
            error: NSError?
        ) {
        }

        override fun receivedError(error: PusherError) {
            onConnectionError?.invoke(error.message())
        }

        override fun subscribedToChannelWithName(name: String) {
            subscribeSuccessCallbacks[name]?.invoke()
        }
    }

    init {
        pusher.setDelegate(delegate)
        pusher.bindWithEventCallback { event ->
            val channelName = event?.channelName() ?: return@bindWithEventCallback
            eventCallbacks[channelName]?.let {
                it(PusherEvent(channelName, event.eventName(), event.data(), event.userId()))
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun connect(
        onConnectionStateChange: (currentState: ConnectionState, previousState: ConnectionState) -> Unit,
        onError: (message: String?, code: String?, e: Exception?) -> Unit,
    ) {
        onConnectionChange = onConnectionStateChange
        onConnectionError = { message ->
            onError(message, null, null)
        }
        pusher.connect()
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun disconnect() {
        pusher.disconnect()
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun subscribe(
        channel: String,
        onSuccess: () -> Unit,
        onEvent: (PusherEvent) -> Unit,
        vararg eventNames: String,
    ) {
        eventCallbacks[channel] = onEvent
        subscribeSuccessCallbacks[channel] = onSuccess
        pusher.subscribeWithChannelName(channel)
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun unsubscribe(channel: String) {
        pusher.unsubscribe(channel)
        eventCallbacks.remove(channel)
        subscribeSuccessCallbacks.remove(channel)
        onAuthenticationFailureCallbacks.remove(channel)
    }

    actual fun subscribePrivate(
        channel: String,
        onSuccess: () -> Unit,
        onAuthenticationFailure: (message: String?, e: Exception?) -> Unit,
        onEvent: (PusherEvent) -> Unit,
        vararg eventNames: String,
    ) {
        eventCallbacks[channel] = onEvent
        subscribeSuccessCallbacks[channel] = onSuccess
        onAuthenticationFailureCallbacks[channel] = onAuthenticationFailure

        pusher.subscribeWithChannelName(channel)
    }

    actual fun subscribePresence(
        channel: String,
        onSuccess: () -> Unit,
        onUsersInformationReceived: (users: Set<User>) -> Unit,
        onUserCountChanged: (count: Long) -> Unit,
        userSubscribed: (user: User) -> Unit,
        userUnsubscribed: (user: User) -> Unit,
        onAuthenticationFailure: (message: String?, e: Exception?) -> Unit,
        onEvent: (PusherEvent) -> Unit,
        vararg eventNames: String,
    ) {
        eventCallbacks[channel] = onEvent
        subscribeSuccessCallbacks[channel] = onSuccess
        onAuthenticationFailureCallbacks[channel] = onAuthenticationFailure

        pusher.subscribeToPresenceChannelWithChannelName(
            channelName = channel,
            onMemberAdded = {
                if (it != null) {
                    userSubscribed(User(id = it.userId(), info = it.userInfo()?.toString()))
                }
            },
            onMemberRemoved = {
                if (it != null) {
                    userUnsubscribed(User(id = it.userId(), info = it.userInfo()?.toString()))
                }
            },
            onSubscriptionCountChanged = {
                onUserCountChanged(it)
            },
        )
    }

}