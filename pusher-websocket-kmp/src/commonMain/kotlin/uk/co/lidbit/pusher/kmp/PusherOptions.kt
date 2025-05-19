package uk.co.lidbit.pusher.kmp

class PusherOptions private constructor(
    val host: String?,
    val proxy: Proxy?,
    val cluster: String?,
    val activityTimeout: Long?,
    val channelAuthorizer: ChannelAuthorizer?,
    val maxReconnectGapInSeconds: Int?,
    val maxReconnectionAttempts: Int?,
    val autoReconnect: Boolean?,
    val pongTimeout: Long?,
    val useTLS: Boolean?,
    val userAuthenticator: UserAuthenticator?,
    val wsPort: Int?,
    val wssPort: Int?,
) {

    class Builder {
        private var host: String? = null
        private var proxy: Proxy? = null
        private var cluster: String? = null
        private var activityTimeout: Long? = null
        private var channelAuthorizer: ChannelAuthorizer? = null
        private var maxReconnectGapInSeconds: Int? = null
        private var maxReconnectionAttempts: Int? = null
        private var autoReconnect: Boolean? = null
        private var pongTimeout: Long? = null
        private var useTLS: Boolean? = null
        private var userAuthenticator: UserAuthenticator? = null
        private var wsPort: Int? = null
        private var wssPort: Int? = null

        fun setHost(host: String): Builder {
            this.host = host
            return this
        }

        fun setProxy(proxy: Proxy): Builder {
            this.proxy = proxy
            return this
        }

        fun setCluster(cluster: String): Builder {
            this.cluster = cluster
            return this
        }

        fun setActivityTimeout(timeout: Long): Builder {
            this.activityTimeout = timeout
            return this
        }

        fun setChannelAuthorizer(authorizer: ChannelAuthorizer): Builder {
            this.channelAuthorizer = authorizer
            return this
        }

        fun setMaxReconnectGapInSeconds(seconds: Int): Builder {
            this.maxReconnectGapInSeconds = seconds
            return this
        }

        fun setMaxReconnectionAttempts(attempts: Int): Builder {
            this.maxReconnectionAttempts = attempts
            return this
        }

        fun setAutoReconnect(autoReconnect: Boolean): Builder {
            this.autoReconnect = autoReconnect
            return this
        }

        fun setPongTimeout(timeout: Long): Builder {
            this.pongTimeout = timeout
            return this
        }

        fun setUseTls(useTLS: Boolean): Builder {
            this.useTLS = useTLS
            return this
        }

        fun setUserAuthenticator(authenticator: UserAuthenticator): Builder {
            this.userAuthenticator = authenticator
            return this
        }

        fun setWsPort(port: Int): Builder {
            this.wsPort = port
            return this
        }

        fun setWssPort(port: Int): Builder {
            this.wssPort = port
            return this
        }

        fun build(): PusherOptions {
            return PusherOptions(
                host = host,
                proxy = proxy,
                cluster = cluster,
                activityTimeout = activityTimeout,
                channelAuthorizer = channelAuthorizer,
                maxReconnectGapInSeconds = maxReconnectGapInSeconds,
                maxReconnectionAttempts = maxReconnectionAttempts,
                autoReconnect = autoReconnect,
                pongTimeout = pongTimeout,
                useTLS = useTLS,
                userAuthenticator = userAuthenticator,
                wsPort = wsPort,
                wssPort = wssPort,
            )
        }
    }

    class Proxy(
        val type: ProxyType,
        val hostname: String,
        val port: Int,
    )

    enum class ProxyType {
        DIRECT,
        HTTP,
        SOCKS,
    }

}