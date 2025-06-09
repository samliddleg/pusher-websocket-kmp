package uk.co.lidbit.pusher.kmp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class HttpChannelAuthorizer actual constructor(
    endpoint: String,
    private val headers: () -> Map<String, String>,
) : ChannelAuthorizer {

    private val authorizer = com.pusher.client.util.HttpChannelAuthorizer(endpoint)

    actual override fun authorize(channelName: String, socketId: String): String {
        authorizer.setHeaders(headers())
        return authorizer.authorize(channelName, socketId)
    }

}