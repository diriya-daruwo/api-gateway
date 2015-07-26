package com.sml.services.apigw

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import spray.can.Http
import com.sml.services.rest.GatewayServiceActor
import com.sml.services.config.Configuration

object Main extends App with Configuration {
  // create an actor system for application
  implicit val system = ActorSystem("rest-service-example")

  // create and start rest service actor
  val restService = system.actorOf(Props[GatewayServiceActor], "rest-endpoint")

  // start HTTP server with rest service actor as a handler
  IO(Http) ! Http.Bind(restService, "localhost", 8080)
}
