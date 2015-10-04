package com.sml.apigw.services


import akka.actor.{Actor, ReceiveTimeout}
import akka.event.Logging
import com.sml.apigw.config.Configuration
import com.sml.apigw.protocols.{User, Appointment}
import org.slf4j.LoggerFactory
import spray.client.pipelining._
import spray.http._
import spray.routing.RequestContext
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import spray.httpx.SprayJsonSupport._


case class GetAppointments()

case class GetAppointment(id: Int)

case class CreateAppointment(appointment: Appointment)

/**
 * Actor class which deals with appointment service
 * @param requestContext spray request context
 */
class AppointmentService(requestContext: RequestContext) extends Actor with Configuration {

  implicit val system = context.system

  import system.dispatcher

  val logger = LoggerFactory.getLogger(this.getClass().getName())
  context.setReceiveTimeout(30 seconds)

  def receive = {
    case GetAppointments() =>
      context.setReceiveTimeout(60 seconds)
      getAppointments()
    case GetAppointment(id) =>
      context.setReceiveTimeout(60 seconds)
      getAppointment(id)
    case CreateAppointment(appointment) =>
      context.setReceiveTimeout(60 seconds)
      createAppointment(appointment)
    case ReceiveTimeout =>
      context.setReceiveTimeout(Duration.Undefined)
      logger.error("Timed out")
    case msg =>
      logger.error("Invalid message received", msg)
  }

  def getAppointments() = {
    import com.sml.apigw.protocols.AppointmentProtocol._

    //    val pipeline = sendReceive ~> unmarshal[Appointment]
    //
    //    val response: Future[Appointment] = pipeline {
    //      Get("http://10.2.2.132:7000/api/v1/appointments/?format=json")
    //    }
    //
    //    response.onComplete {
    //      case Success(appointmentResponse) =>
    //        logger.debug(appointmentResponse.toString)
    //        requestContext.complete(StatusCodes.Created -> appointmentResponse)
    //      case Failure(e) =>
    //        logger.error(e.toString)
    //        requestContext.complete(e)
    //    }

    // Return sample response
    val doctor = User(None, "eran@gmail.com", Some(3), "Comviq")
    val patient = User(None, "pat@gmail.com", Some(4), "Saman")
    val b = Appointment(Some("12"), patient, doctor, "2013-12-23")
    val l = (b, b, b, b)
    requestContext.complete(l)
  }

  def getAppointment(id: Int) = {
    import com.sml.apigw.protocols.AppointmentProtocol._

    //    val pipeline = sendReceive ~> unmarshal[Appointment]
    //
    //    val response: Future[Appointment] = pipeline {
    //      Get(s"http://10.2.2.132:7000/api/v1/appointments/$id/?format=json")
    //    }
    //
    //    response.onComplete {
    //      case Success(appointment) =>
    //        logger.debug(appointment.toString)
    //        requestContext.complete(StatusCodes.Created -> appointment)
    //      case Failure(e) =>
    //        logger.error(e.toString)
    //        requestContext.complete(e)
    //    }

    // return sample appointment
    val doctor = User(None, "eran@gmail.com", Some(3), "Comviq")
    val patient = User(None, "pat@gmail.com", Some(4), "Saman")
    val appointment = Appointment(Some("12"), patient, doctor, "2013-12-23")
    requestContext.complete(appointment)
  }

  def createAppointment(appointment: Appointment) = {
    import com.sml.apigw.protocols.AppointmentProtocol._

    //    val pipeline = sendReceive
    //
    //    val response = pipeline {
    //      Post("http://10.2.2.132:7000/api/v1/appointments/", appointment)
    //    }
    //
    //    response.onComplete {
    //      case Success(_) =>
    //        logger.debug("user created")
    //        requestContext.complete(StatusCodes.Created -> "created")
    //      case Failure(e) =>
    //        logger.error(e.toString)
    //        requestContext.complete(e)
    //    }

    // return sample response
    requestContext.complete(StatusCodes.Created -> "created")
  }

}
