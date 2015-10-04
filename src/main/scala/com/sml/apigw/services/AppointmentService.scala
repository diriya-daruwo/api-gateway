package com.sml.apigw.services

import akka.actor.Actor
import akka.event.Logging
import com.sml.apigw.protocols.Appointment
import spray.routing.RequestContext
import spray.httpx.SprayJsonSupport._


/**
 * Created by eranga on 8/6/15.
 */
object AppointmentService {

  case class GetAppointment()

  case class CreateAppointment(appointment: Appointment)

}

class AppointmentService(requestContext: RequestContext) extends Actor {

  import AppointmentService._

  implicit val system = context.system
  val log = Logging(system, getClass)

  def receive = {
    case Get() =>
      // get request to appointment service
      getAllAppointments()
      context.stop(self)
    case _ =>
      val b = Appointment("12", "Pagero")
      b
  }

  def getAllAppointments() = {
    import com.sml.apigw.protocols.AppointmentProtocol._

    val b = Appointment("12", "Pagero")
    val l = (b, b, b, b)
    requestContext.complete(l)
  }

  def createAppointment(appointment: Appointment) = {

  }
}
