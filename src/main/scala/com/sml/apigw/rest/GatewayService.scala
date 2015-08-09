package com.sml.apigw.rest

import akka.actor.Actor
import akka.event.slf4j.SLF4JLogging
import com.sml.apigw.protocols.{Appointment, Prescription, User}
import spray.http._
import spray.routing.HttpService

import scala.concurrent.Future

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

  val router =
    pathPrefix("api" / "v1") {
      path("appointments") {
        import com.sml.apigw.protocols.AppointmentProtocol._
        get {
          complete {
            getAppointments()
          }
        } ~
          post {
            entity(as[Appointment]) { appointment =>
              complete {
                StatusCodes.Created -> createAppointment(appointment)
              }
            }
          }
      } ~
        path("prescriptions") {
          import com.sml.apigw.protocols.PrescriptionProtocol._
          get {
            complete {
              getPrescriptions()
            }
          } ~
            post {
              entity(as[Prescription]) { prescription =>
                complete {
                  StatusCodes.Created -> createPrescription(prescription)
                }
              }
            }
        } ~
        path("users") {
          import com.sml.apigw.protocols.UserProtocol._
          get {
            complete {
              getUsers()
            }
          } ~
            post {
              entity(as[User]) { user =>
                complete {
                  StatusCodes.Created -> createUser(user)
                }
              }
            }
        }
    }

  def getAppointments() = Future[List[Appointment]] {
    // TODO call for appointment service
    val b = new Appointment("1", "2")
    List(b, b, b, b)
  }

  def getPrescriptions() = Future[List[Prescription]] {
    // TODO call for prescription service
    val p = new Prescription("1", "2", "34")
    List(p, p, p)
  }

  def getUsers() = Future[List[User]] {
    // TODO call for user service
    val u = new User("1", "2", "34", "admin")
    List(u, u, u)
  }

  def createAppointment(appointment: Appointment) = Future[String] {
    // TODO call for appointment service to create Appointment
    "created"
  }

  def createPrescription(prescription: Prescription) = Future[String] {
    // TODO call for prescription service to create Prescription
    "created"
  }

  def createUser(user: User) = Future[String] {
    // TODO call for user service to create User
    "created"
  }

}

