package uk.co.lidbit.pusher.kmp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class HttpUserAuthenticator actual constructor(
    endpoint: String,
    headers: Map<String, String>,
) : UserAuthenticator {

    private val authenticator = com.pusher.client.util.HttpUserAuthenticator(endpoint)

    init {
        authenticator.setHeaders(headers)
    }

    actual override fun authenticate(socketId: String): String =
        authenticator.authenticate(socketId)
}