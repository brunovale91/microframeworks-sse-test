package org.swarmbit;

import akka.NotUsed;
import akka.actor.typed.Behavior;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.server.Route;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.ActorSystem;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletionStage;

public class StreamApp {

    static void startHttpServer(Route route, ActorSystem<?> system) {
        CompletionStage<ServerBinding> futureBinding =
            Http.get(system).newServerAt("localhost", 8082).bind(route);

        futureBinding.whenComplete((binding, exception) -> {
            if (binding != null) {
                InetSocketAddress address = binding.localAddress();
                system.log().info("Server online at http://{}:{}/",
                    address.getHostString(),
                    address.getPort());
            } else {
                system.log().error("Failed to bind HTTP endpoint, terminating system", exception);
                system.terminate();
            }
        });
    }

    public static void main(String[] args) throws Exception {
        Behavior<NotUsed> rootBehavior = Behaviors.setup(context -> {
            StreamRoutes streamRoutes = new StreamRoutes();
            startHttpServer(streamRoutes.streamRoute(), context.getSystem());
            return Behaviors.empty();
        });
        ActorSystem.create(rootBehavior, "StreamServer");
    }

}


