package com.sml.services.rest

import akka.actor.{ActorLogging, ActorRefFactory, Actor}
import spray.routing.Route
import com.sml.services.protocols.AppointmentProtocol.Appointment
import spray.http.{MediaTypes, MediaType}
import spray.routing.{HttpServiceActor, HttpService}

/**
 * Actor class of GatewayService
 *
 * @author eranga herath(erangaeb@gmail.com)
 */
class GatewayServiceActor extends Actor with GatewayService {
  implicit def actorRefFactory = context
  override def receive = runRoute(routes)
}

