package com.sml.apigw.boot

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import com.sml.apigw.config.Configuration
import com.sml.apigw.rest.GatewayServiceActor
import spray.can.Http

object Main extends App with Configuration {
  // create an actor system for application
  implicit val system = ActorSystem("rest-service")

  // create and start rest service actor
  val restService = system.actorOf(Props[GatewayServiceActor], "gateway-rest")

  // start HTTP server with rest service actor as a handler
  IO(Http) ! Http.Bind(restService, serviceHost, servicePort)
}
