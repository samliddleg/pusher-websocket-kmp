package uk.co.lidbit.pusher.kmp

import com.pusher.client.connection.ConnectionState
import com.pusher.client.util.ConnectionFactory
import com.pusher.client.util.HttpChannelAuthorizer
import com.pusher.client.util.HttpUserAuthenticator
import java.net.InetSocketAddress
import java.net.Proxy

internal fun PusherOptions.toAbstracted() = com.pusher.client.PusherOptions().apply {
    this@toAbstracted.host?.let { setHost(it) }
    this@toAbstracted.proxy?.let {
        setProxy(
            Proxy(
                it.type.toJava(),
                InetSocketAddress(it.hostname, it.port)
            )
        )
    }
    this@toAbstracted.cluster?.let {
        setCluster(it)
    }
    this@toAbstracted.activityTimeout?.let { setActivityTimeout(it) }
    this@toAbstracted.channelAuthorizer?.let {
        setChannelAuthorizer { channelName, socketId -> it.authorize(channelName, socketId) }
    }
    this@toAbstracted.maxReconnectGapInSeconds?.let { setMaxReconnectGapInSeconds(it) }
    this@toAbstracted.maxReconnectionAttempts?.let { setMaxReconnectionAttempts(it) }
    this@toAbstracted.pongTimeout?.let { setPongTimeout(it) }
    this@toAbstracted.useTLS?.let { setUseTLS(it) }
    this@toAbstracted.userAuthenticator?.let {
        setUserAuthenticator { socketId -> it.authenticate(socketId) }
    }
    this@toAbstracted.wsPort?.let { setWsPort(it) }
    this@toAbstracted.wssPort?.let { setWssPort(it) }
}

internal fun PusherOptions.ProxyType.toJava() = when (this) {
    PusherOptions.ProxyType.DIRECT -> Proxy.Type.DIRECT
    PusherOptions.ProxyType.HTTP -> Proxy.Type.HTTP
    PusherOptions.ProxyType.SOCKS -> Proxy.Type.SOCKS
}

internal fun ConnectionState.toAbstracted() = when (this) {
    ConnectionState.CONNECTING -> uk.co.lidbit.pusher.kmp.ConnectionState.CONNECTING
    ConnectionState.CONNECTED -> uk.co.lidbit.pusher.kmp.ConnectionState.CONNECTED
    ConnectionState.DISCONNECTING -> uk.co.lidbit.pusher.kmp.ConnectionState.DISCONNECTING
    ConnectionState.DISCONNECTED -> uk.co.lidbit.pusher.kmp.ConnectionState.DISCONNECTED
    ConnectionState.RECONNECTING -> uk.co.lidbit.pusher.kmp.ConnectionState.RECONNECTING
    ConnectionState.ALL -> uk.co.lidbit.pusher.kmp.ConnectionState.ALL
}

internal fun com.pusher.client.channel.PusherEvent.toAbstracted() = PusherEvent(
    channelName = channelName,
    eventName = eventName,
    data = data,
    userId = userId,
)

internal fun com.pusher.client.channel.User.toAbstracted() = User(
    id = id,
    info = info,
)