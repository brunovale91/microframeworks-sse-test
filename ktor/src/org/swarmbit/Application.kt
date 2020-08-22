package org.swarmbit

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay

fun main() {
    embeddedServer(Netty, port = 8083) {
        routing {
            get("/ktor/stream") {
                val channel = produce {
                    var n = 0
                    while (true) {
                        send(SseEvent(n))
                        delay(5000)
                        n++
                    }
                }.broadcast()
                val events = channel.openSubscription()
                try {
                    call.respondSse(events)
                } finally {
                    events.cancel()
                }
            }
        }
    }.start(wait = true)
}


data class SseEvent(val counter: Int)


suspend fun ApplicationCall.respondSse(events: ReceiveChannel<SseEvent>) {
    response.cacheControl(CacheControl.NoCache(null))
    respondTextWriter(contentType = ContentType.Text.EventStream) {
        for (event in events) {
            write("data: ${event.counter}\n")
            write("\n")
            flush()
        }
    }
}