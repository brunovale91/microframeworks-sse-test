package org.swarmbit

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.sse.Event
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit

@Controller("/micronaut/stream")
class StreamController {

    @ExecuteOn(TaskExecutors.IO)
    @Get(produces = [MediaType.TEXT_EVENT_STREAM])
    fun stream(): Publisher<Event<Counter>> {
        return Flowable.interval(5, TimeUnit.SECONDS).map {
            createEvent(it, Counter(it))
        }
    }

    private fun createEvent(index: Long, counter: Counter): Event<Counter> {
        return Event.of(counter).id(index.toString()).name("counter")
    }

}

data class Counter(var number: Long)