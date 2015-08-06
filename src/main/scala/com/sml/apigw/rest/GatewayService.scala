package com.sml.apigw.rest

import akka.actor.Actor
import akka.event.slf4j.SLF4JLogging
import com.sml.apigw.auth.BasicAuthenticator
import com.sml.apigw.protocols.{Appointment, Prescription}
import spray.http.MediaTypes
import spray.httpx.SprayJsonSupport._
import spray.routing.HttpService

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Actor class of GatewayService
 *
 * @author eranga herath(erangaeb@gmail.com)
 */
class GatewayServiceActor extends Actor with GatewayService {
  implicit def actorRefFactory = context

  def receive = runRoute(appointmentRouter ~ prescriptionRouter)
}

/**
 * API gateway service, define REST routers in here
 */
trait GatewayService extends HttpService with BasicAuthenticator with SLF4JLogging {

  import com.sml.apigw.protocols.AppointmentProtocol._
  import com.sml.apigw.protocols.PrescriptionProtocol._

  val appointmentRouter =
    pathPrefix("api" / "v1") {
      path("appointments") {
        authenticate(basicAuthenticator) { user =>
          get {
            respondWithMediaType(MediaTypes.`application/json`) {
              complete {
                // TODO get appointments via appointment service
                // TODO delegate to actor
                log.debug("GET all bills: %s".format(user.username))
                val b = Appointment("12", "Pagero")
                val l = (b, b, b, b)
                l
              }
            }
          } ~
            post {
              entity(as[Appointment]) { appointment =>
                complete {
                  Console.println("created")
                  Console.println(appointment.patient)
                  log.debug("POST bill: %s".format(appointment))
                  "POST bill"
                }
              }
            }
        }
      } ~
        path("appointments" / LongNumber) { appointmentId =>
          authenticate(basicAuthenticator) { user =>
            get {
              complete {
                // TODO get appointments via appointment service
                // TODO delegate to actor
                //log.debug("GET all bills: %l".format(appointmentId))
                val b = Appointment("12", "Pagero")
                b
              }
            }
          }
        }
    }

  val prescriptionRouter =
    pathPrefix("api" / "v1") {
      path("prescriptions") {
        authenticate(basicAuthenticator) { user =>
          get {
            respondWithMediaType(MediaTypes.`application/json`) {
              complete {
                // TODO get get prescriptions via prescription service
                // TODO delegate to actor
                log.debug("GET all bills: %s".format(user.username))
                val b = Prescription("12", "1", "2")
                val l = (b, b, b, b)
                l
              }
            }
          } ~
            post {
              entity(as[Prescription]) { prescription =>
                complete {
                  Console.println("created")
                  Console.println(prescription.patient)
                  log.debug("POST bill: %s".format(prescription))
                  "POST bill"
                }
              }
            }
        }
      } ~
        path("prescriptions" / LongNumber) { prescriptionId =>
          authenticate(basicAuthenticator) { user =>
            get {
              complete {
                // TODO get prescription via prescription service
                // TODO delegate to actor
                //log.debug("GET all bills: %l".format(appointmentId))
                val b = Prescription("12", "Pagero", "jaaaa")
                b
              }
            }
          }
        }
    }

}
