package uk.co.lidbit.pusher.kmp

fun interface UserAuthenticator {

    fun authenticate(socketId: String): String

}

expect class HttpUserAuthenticator(endpoint: String, headers: () -> Map<String, String> = emptyMap()) :
    UserAuthenticator {
    override fun authenticate(socketId: String): String
}