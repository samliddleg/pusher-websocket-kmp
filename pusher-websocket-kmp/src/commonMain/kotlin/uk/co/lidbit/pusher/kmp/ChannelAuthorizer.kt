package uk.co.lidbit.pusher.kmp

fun interface ChannelAuthorizer {

    fun authorize(channelName: String, socketId: String): String

}

expect abstract class HttpChannelAuthorizer(
    endpoint: String,
) : ChannelAuthorizer {

    override fun authorize(channelName: String, socketId: String): String

    abstract fun headers(): Map<String, String>

}
