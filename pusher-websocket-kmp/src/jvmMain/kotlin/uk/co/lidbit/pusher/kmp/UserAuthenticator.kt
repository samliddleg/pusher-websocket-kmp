package uk.co.lidbit.pusher.kmp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class HttpUserAuthenticator actual constructor(
    endpoint: String,
    private val headers: () -> Map<String, String>,
) : UserAuthenticator {

    private val authenticator = com.pusher.client.util.HttpUserAuthenticator(endpoint)

    actual override fun authenticate(socketId: String): String {
        authenticator.setHeaders(headers())
        return authenticator.authenticate(socketId)
    }
}