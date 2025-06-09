package uk.co.lidbit.pusher.kmp

fun interface ChannelAuthorizer {

    fun authorize(channelName: String, socketId: String): String

}

expect class HttpChannelAuthorizer(
    endpoint: String,
    headers: () -> Map<String, String>,
) : ChannelAuthorizer {

    override fun authorize(channelName: String, socketId: String): String

}
