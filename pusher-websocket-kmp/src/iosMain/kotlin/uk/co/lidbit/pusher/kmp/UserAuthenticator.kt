package uk.co.lidbit.pusher.kmp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class HttpUserAuthenticator actual constructor(
    val endpoint: String,
    val headers: () -> Map<String, String>,
) : UserAuthenticator {

    actual override fun authenticate(socketId: String): String = endpoint

}