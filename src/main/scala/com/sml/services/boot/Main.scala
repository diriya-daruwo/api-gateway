package com.sml.services.boot

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import com.sml.services.config.Configuration
import com.sml.services.rest.GatewayServiceActor
import spray.can.Http

object Main extends App with Configuration {
  // create an actor system for application
  implicit val system = ActorSystem("rest-service")

  // create and start rest service actor
  val restService = system.actorOf(Props[GatewayServiceActor], "gateway-rest")

  // start HTTP server with rest service actor as a handler
  IO(Http) ! Http.Bind(restService, serviceHost, servicePort)
}
