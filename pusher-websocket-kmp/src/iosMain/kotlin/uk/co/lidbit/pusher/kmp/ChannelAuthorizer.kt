package uk.co.lidbit.pusher.kmp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual abstract class HttpChannelAuthorizer actual constructor(
    val endpoint: String,
) : ChannelAuthorizer {

    actual override fun authorize(channelName: String, socketId: String): String = endpoint

    actual abstract fun headers(): Map<String, String>

}