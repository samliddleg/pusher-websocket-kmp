package uk.co.lidbit.pusher.kmp

data class PusherEvent(
    val channelName: String?,
    val eventName: String?,
    val data: String?,
    val userId: String?,
)
