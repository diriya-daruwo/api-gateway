package com.sml.apigw.rest

import akka.actor.{Actor, Props}
import com.sml.apigw.auth.BasicAuthenticator
import com.sml.apigw.services.ElevationService
import spray.routing._

import scala.concurrent.ExecutionContext.Implicits.global

class SprayApiDemoServiceActor extends Actor with SprayApiDemoService {

  def actorRefFactory = context

  def receive = runRoute(sprayApiDemoRoute)
}

trait SprayApiDemoService extends HttpService with BasicAuthenticator {
  val sprayApiDemoRoute =
    pathPrefix("api" / "v1") {
      authenticate(basicAuthenticator) { user =>
        path("appointments" / DoubleNumber / DoubleNumber) { (long, lat) =>
          requestContext =>
            val elevationService = actorRefFactory.actorOf(Props(new ElevationService(requestContext)))
            elevationService ! ElevationService.Process(long, lat)
        }
      }
    }
}
