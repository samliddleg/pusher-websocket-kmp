package uk.co.lidbit.pusher.kmp

import cocoapods.PusherSwift.AuthRequestBuilderProtocolProtocol
import cocoapods.PusherSwift.ConnectionState
import cocoapods.PusherSwift.ConnectionStateConnected
import cocoapods.PusherSwift.ConnectionStateConnecting
import cocoapods.PusherSwift.ConnectionStateDisconnected
import cocoapods.PusherSwift.ConnectionStateDisconnecting
import cocoapods.PusherSwift.ConnectionStateReconnecting
import cocoapods.PusherSwift.OCAuthMethod
import cocoapods.PusherSwift.OCPusherHost
import cocoapods.PusherSwift.PusherClientOptions
import cocoapods.PusherSwift.create
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.HTTPBody
import platform.Foundation.HTTPMethod
import platform.Foundation.NSData
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSNumber
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.Foundation.dataWithBytes
import platform.Foundation.setValue
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal fun PusherOptions.toSwift(): PusherClientOptions {
    val authProtocol: AuthRequestBuilderProtocolProtocol? = when (channelAuthorizer) {
        is HttpChannelAuthorizer -> {
            object : NSObject(), AuthRequestBuilderProtocolProtocol {
                override fun requestForSocketID(
                    socketID: String,
                    channelName: String,
                ): NSURLRequest? {
                    val url = NSURL(string = channelAuthorizer.endpoint)
                    val request = NSMutableURLRequest.requestWithURL(url).apply {
                        setAllowsCellularAccess(true)
                        HTTPMethod = "POST"
                    }
                    request.setValue(
                        "application/x-www-form-urlencoded",
                        forHTTPHeaderField = "Content-Type"
                    )
                    channelAuthorizer.headers().forEach { (key, value) ->
                        request.setValue(value, forHTTPHeaderField = key)
                    }
                    val body = "socket_id=$socketID&channel_name=$channelName"
                    val data = body.encodeToByteArray().usePinned {
                        NSData.dataWithBytes(it.addressOf(0), it.get().size.toULong())
                    }
                    request.HTTPBody = data
                    return request
                }
            }
        }

        is ChannelAuthorizer -> {
            object : NSObject(), AuthRequestBuilderProtocolProtocol {
                override fun requestForSocketID(
                    socketID: String,
                    channelName: String
                ): NSURLRequest? {
                    return channelAuthorizer.authorize(channelName, socketID).let { urlString ->
                        val url = NSURL(string = urlString)
                        val request = NSMutableURLRequest.requestWithURL(url).apply {
                            setAllowsCellularAccess(true)
                            HTTPMethod = "POST"
                        }
                        request
                    }
                }
            }
        }

        null -> null
    }
    val ocAuthMethod = authProtocol?.let { OCAuthMethod(authRequestBuilder = it) } ?: OCAuthMethod()
    val ocHost = if (!cluster.isNullOrBlank()) {
        OCPusherHost(cluster = cluster)
    } else if (!host.isNullOrBlank()) {
        OCPusherHost(host = host)
    } else {
        throw IllegalArgumentException("You must provide a host or cluster to PusherOptions")
    }
    return PusherClientOptions.create(
        ocAuthMethod = ocAuthMethod,
        attemptToReturnJSONObject = false,
        autoReconnect = autoReconnect != false,
        ocHost = ocHost,
        port = (if (useTLS == true) wssPort else wsPort)?.let { NSNumber(it) },
        useTLS = useTLS == true,
        activityTimeout = activityTimeout?.let { NSNumber(long = it) },
    )
}

@OptIn(ExperimentalForeignApi::class)
internal fun ConnectionState.toAbstracted() = when (this) {
    ConnectionStateConnecting -> uk.co.lidbit.pusher.kmp.ConnectionState.CONNECTING
    ConnectionStateConnected -> uk.co.lidbit.pusher.kmp.ConnectionState.CONNECTED
    ConnectionStateDisconnecting -> uk.co.lidbit.pusher.kmp.ConnectionState.DISCONNECTING
    ConnectionStateDisconnected -> uk.co.lidbit.pusher.kmp.ConnectionState.DISCONNECTED
    ConnectionStateReconnecting -> uk.co.lidbit.pusher.kmp.ConnectionState.RECONNECTING
    else -> uk.co.lidbit.pusher.kmp.ConnectionState.ALL
}
