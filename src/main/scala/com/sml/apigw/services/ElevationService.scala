package com.sml.apigw.services

import akka.actor.Actor
import akka.event.Logging
import com.sml.apigw.protocols.{Appointment, Elevation, GoogleElevationApiResult}
import spray.client.pipelining._
import spray.httpx.SprayJsonSupport
import spray.routing.RequestContext

import scala.util.{Failure, Success}

object ElevationService {

  case class Process(long: Double, lat: Double)

}

class ElevationService(requestContext: RequestContext) extends Actor {

  import ElevationService._

  implicit val system = context.system

  import system.dispatcher

  val log = Logging(system, getClass)

  def receive = {
    case Process(long, lat) =>
      process(long, lat)
      context.stop(self)
  }

  def process(long: Double, lat: Double) = {
    log.info("Requesting elevation long: {}, lat: {}", long, lat)
    import SprayJsonSupport._
    import com.sml.apigw.protocols.ElevationProtocol._
    import com.sml.apigw.protocols.AppointmentProtocol._

    val pipeline = sendReceive ~> unmarshal[GoogleElevationApiResult[Elevation]]

    val responseFuture = pipeline {
      Get(s"http://maps.googleapis.com/maps/api/elevation/json?locations=$long,$lat&sensor=false")
    }

    responseFuture onComplete {
      case Success(GoogleElevationApiResult(_, Elevation(_, elevation) :: _)) =>
        log.info("The elevation is: {} m", elevation)
        requestContext.complete(elevation.toString)
      case Failure(error) =>
        log.error("Error occurs: ", error)
        requestContext.complete(error)
    }
  }
}
