package com.sml.services.rest

import akka.event.slf4j.SLF4JLogging
import com.sml.services.protocols.Appointment
import spray.http.MediaTypes
import spray.httpx.SprayJsonSupport._
import spray.routing.HttpService
import spray.routing.authentication.{BasicAuth, UserPass}
import spray.routing.directives.AuthMagnet

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._


case class User(username: String, hashedPassword: String) {
  def isAuthenticatedUser(password: String): Boolean = password.equalsIgnoreCase(hashedPassword)
}

case class AuthInfo(user: User) {
  def hasPermission(permission: String) = true
}

trait BasicAuthenticator {
  def basicAuthenticator(implicit executor: ExecutionContext): AuthMagnet[User] = {
    def authenticator(userPass: Option[UserPass]): Future[Option[User]] = {
      Future {
        // TODO communicate with auth service and authenticate user
        // TODO database query or any other option to find matching user
        if (userPass.exists(up => up.user == "john" && up.pass == "123")) Some(User(username = "eranga", hashedPassword = "123"))
        else None
      }
    }

    BasicAuth(authenticator _, realm = "Private API")
  }
}

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
          }
        }
      }
    }

}

