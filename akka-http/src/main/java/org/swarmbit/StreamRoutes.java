package org.swarmbit;

import java.time.Duration;
import java.util.Random;

import akka.http.javadsl.marshalling.sse.EventStreamMarshalling;
import akka.http.javadsl.model.sse.ServerSentEvent;
import akka.stream.javadsl.Source;

import static akka.http.javadsl.server.Directives.*;
import static java.time.temporal.ChronoUnit.SECONDS;

import akka.http.javadsl.server.Route;
import akka.NotUsed;

public class StreamRoutes {

  public Route streamRoute() {
    return path("akka-http-stream", () -> get(() -> {
                    Random random = new Random();
                    Source<ServerSentEvent, NotUsed> source = Source.tick(
                                Duration.of(5, SECONDS),
                                Duration.of(5, SECONDS),
                                NotUsed.notUsed()
                    )
                    .map(a -> random.nextInt())
                    .map(a -> ServerSentEvent.create(String.valueOf(a)))
                    .mapMaterializedValue(c -> NotUsed.notUsed());
                    return completeOK(source, EventStreamMarshalling.toEventStream());
                })
    );
  }
}
