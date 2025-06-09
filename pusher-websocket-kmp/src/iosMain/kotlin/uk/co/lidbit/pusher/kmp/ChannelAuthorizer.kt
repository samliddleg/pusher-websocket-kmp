package uk.co.lidbit.pusher.kmp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class HttpChannelAuthorizer actual constructor(
    val endpoint: String,
    val headers: () -> Map<String, String>,
) : ChannelAuthorizer {

    actual override fun authorize(channelName: String, socketId: String): String = endpoint

}