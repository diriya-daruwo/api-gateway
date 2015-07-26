package com.sml.services.rest

import akka.event.slf4j.SLF4JLogging
import com.sml.services.protocols.AppointmentProtocol.Appointment
import spray.http.MediaTypes
import spray.routing.HttpService

/**
 * API gateway service, define REST routers in here
 */
trait GatewayService extends HttpService with SLF4JLogging {

  import com.sml.services.protocols.AppointmentProtocol.Protocol._

  //var quizzes = Vector[Appointment]()

  val routes = {
    pathPrefix("api" / "v1") {
      path("appointment") {
        get {
          respondWithMediaType(MediaTypes.`application/json`) {
            complete {
              Appointment("1", "eranga")
            }
          }
        }
      }
    }
  }

  //    get { path("appointment") {
  //        complete {
  //          "Hello World!"
  //        }
  //      }
  //    }
}

