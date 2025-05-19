package uk.co.lidbit.pusher.kmp.example

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Pusher Websocket KMP",
    ) {
        App()
    }
}