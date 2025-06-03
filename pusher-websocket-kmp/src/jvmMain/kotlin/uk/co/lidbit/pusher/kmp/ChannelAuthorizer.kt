package uk.co.lidbit.pusher.kmp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual abstract class HttpChannelAuthorizer actual constructor(
    endpoint: String,
) : ChannelAuthorizer {

    private val authorizer = com.pusher.client.util.HttpChannelAuthorizer(endpoint)

    init {
        authorizer.setHeaders(headers())
    }

    actual override fun authorize(channelName: String, socketId: String): String =
        authorizer.authorize(channelName, socketId)

    actual abstract fun headers(): Map<String, String>

}