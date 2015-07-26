package com.sml.apigw.rest

import akka.actor.{Actor, Props}
import com.sml.apigw.services.ElevationService
import spray.routing._

class SprayApiDemoServiceActor extends Actor with SprayApiDemoService {

  def actorRefFactory = context

  def receive = runRoute(sprayApiDemoRoute)
}

trait SprayApiDemoService extends HttpService {
  val sprayApiDemoRoute =
    pathPrefix("api/v1") {
      path("appointments" / DoubleNumber / DoubleNumber) { (long, lat) =>
        requestContext =>
          val elevationService = actorRefFactory.actorOf(Props(new ElevationService(requestContext)))
          elevationService ! ElevationService.Process(long, lat)
      }
    }
}
