package com.sml.services.rest

import akka.event.slf4j.SLF4JLogging
import com.sml.services.auth.BasicAuthenticator
import com.sml.services.protocols.Appointment
import spray.http.MediaTypes
import spray.httpx.SprayJsonSupport._
import spray.routing.HttpService

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * API gateway service, define REST routers in here
 */
trait GatewayService extends HttpService with BasicAuthenticator with SLF4JLogging {

  import com.sml.services.protocols.AppointmentProtocol._

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
                log.debug("POST bill: %s".format(appointment))
                "POST bill"
              }
            }
          }
        }
      }
    }

}

