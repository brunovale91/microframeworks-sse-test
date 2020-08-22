import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.*
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

const val outerLoop = 20
const val innerLoop = 500
const val delay = 1000L
const val timeout = 20000L

fun main() {
    runBlocking {
        launch { runSseTest("8083", "micronaut/stream") }
        launch { runSseTest("8081","quarkus/stream") }
        launch { runSseTest("8082","akka-http-stream") }
        launch { runSseTest("8083", "ktor/stream") }
    }
}

private suspend fun runSseTest(
        port: String,
        path: String
) {
    coroutineScope {
        val countSuccess: AtomicInteger = AtomicInteger(outerLoop * innerLoop)
        val clients = mutableListOf<HttpClient>()
        try {
            val requests = mutableListOf<Deferred<ByteArray>>()
            for (i in 1..outerLoop) {
                val client = HttpClient(CIO)
                clients.add(client)
                for (j in 1..innerLoop) {
                    requests.add(async {
                        var byteArray = ByteArray(0)
                        try {
                            byteArray = client.get("http://localhost:$port/$path")
                        } catch (e: Exception) {
                            countSuccess.decrementAndGet()
                        }
                        byteArray
                    })
                }
                delay(delay)
            }
            println("Created ${requests.size} - $path")
            launch {
                println("Waiting $timeout millis to cancel all streams - $path!!!")
                delay(timeout)
                println("Finished success - ${countSuccess.get()} - $path!!!")
                requests.forEach { it.cancel() }
            }
            requests.forEach { it.await() }
        } catch (e: Exception) {
        } finally {
            clients.forEach { it.close() }
        }
    }
}
