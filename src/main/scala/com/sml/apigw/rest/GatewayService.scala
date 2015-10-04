package com.sml.apigw.rest

import akka.actor.{Props, Actor}
import akka.event.slf4j.SLF4JLogging
import akka.event.Logging
import com.sml.apigw.protocols._
import com.sml.apigw.services._
import org.slf4j.LoggerFactory
import spray.client.pipelining._
import spray.http._
import spray.routing.HttpService

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * Actor class of GatewayService
 *
 * @author eranga herath(erangaeb@gmail.com)
 */
class GatewayServiceActor extends Actor with GatewayService {
  implicit def actorRefFactory = context

  def receive = runRoute(router)
}

/**
 * API gateway service, define REST routers in here
 * Following are the available endpoints
 * 1. api/v1/appointments
 * 2. api/v1/prescriptions
 * 3. api/v1/users
 */
trait GatewayService extends HttpService with SLF4JLogging {

  import spray.httpx.SprayJsonSupport._

  implicit def executionContext = actorRefFactory.dispatcher

  val logger = LoggerFactory.getLogger(this.getClass().getName())

  val router =
    pathPrefix("api" / "v1") {
      path("appointments") {
        import com.sml.apigw.protocols.AppointmentProtocol._
        get { requestContext =>
          val appointmentService = actorRefFactory.actorOf(Props(new AppointmentService(requestContext)))
          appointmentService ! GetAppointments()
        } ~
          post {
            entity(as[Appointment]) { appointment => requestContext =>
              val appointmentService = actorRefFactory.actorOf(Props(new AppointmentService(requestContext)))
              appointmentService ! CreateAppointment(appointment)
            }
          }
      } ~
        path("appointments" / IntNumber) { appointmentId =>
          get { requestContext =>
            val appointmentService = actorRefFactory.actorOf(Props(new AppointmentService(requestContext)))
            appointmentService ! GetAppointment(appointmentId)
          }
        } ~
        path("users") {
          import com.sml.apigw.protocols.UserProtocol._
          get { requestContext =>
            val userService = actorRefFactory.actorOf(Props(new UserService(requestContext)))
            userService ! GetUsers()
          } ~
            post {
              entity(as[User]) { user => requestContext =>
                val userService = actorRefFactory.actorOf(Props(new UserService(requestContext)))
                userService ! CreateUser(user)
              }
            }
        } ~
        path("users" / IntNumber) { user_id =>
          get { requestContext =>
            val userService = actorRefFactory.actorOf(Props(new UserService(requestContext)))
            userService ! GetUser(user_id)
          }
        }
    }
}

