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
 */
trait GatewayService extends HttpService with SLF4JLogging {

  import spray.httpx.SprayJsonSupport._

  implicit def executionContext = actorRefFactory.dispatcher

  val router =
    pathPrefix("api" / "v1") {
      path("appointments") {
        import com.sml.apigw.protocols.AppointmentProtocol._
        get {
          //val appointmentService = actorRefFactory.actorOf(Props(new AppointmentService(requestContext)))
          //appointmentService ! AppointmentService.Get()
          complete {
            //(worker ? GetAppointment).mapTo[Appointment]
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
            //val appointmentService = actorRefFactory.actorOf(Props(new AppointmentService(requestContext)))
            //appointmentService ! AppointmentService.Get()
            //complete {
            //  (worker ? GetPrescriptions).mapTo[Prescription]
            //}
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
            //val appointmentService = actorRefFactory.actorOf(Props(new AppointmentService(requestContext)))
            //appointmentService ! AppointmentService.Get()
            //complete {
            //  (worker ? GetPrescriptions).mapTo[Prescription]
            //}
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
    val l = List(b, b, b, b)
    l
  }

  def getPrescriptions() = Future[Prescription] {
    // TODO call for prescription service
    new Prescription("1", "2", "34")
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

