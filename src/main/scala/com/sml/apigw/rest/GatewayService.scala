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
          import com.sml.apigw.protocols.SmlUserProtocol._
          get { requestContext =>
            val userService = actorRefFactory.actorOf(Props(new UserService(requestContext)))
            userService ! GetUsers()
          } ~
            post {
              entity(as[SmlUser]) { smlUser => requestContext =>
                val userService = actorRefFactory.actorOf(Props(new UserService(requestContext)))
                userService ! CreateUser(smlUser)
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
    import com.sml.apigw.protocols.DeviceProtocol._
    val pipeline = (
      addHeader("Authorization", "Basic YWRtaW46YWRtaW4=")
        ~> sendReceive
        ~> unmarshal[DeviceResponse]
      )

    val response = pipeline {
      Get("http://10.2.2.132:8080/api/v1/devices/?format=json") ~> addCredentials(BasicHttpCredentials("eranga", "123"))
    }

    response.onComplete {
      case Success(resp) => print(resp)
      case Failure(e) => print(e.toString)
    }

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

  def createUser(user: SmlUser) = Future[String] {
    import com.sml.apigw.protocols.SmlUserProtocol._

    // TODO call for user service to create User
    println(user.name)
    println(user.email)
    println(user.id)

    val pipeline = sendReceive

    val response = pipeline {
      Post("http://10.2.2.132:9000/api/v1/users/", user)
    }

    response.onComplete {
      case Success(resp) =>
        "created"
      case Failure(e) =>
        "fail"
      case _ =>
        "fail"
    }

    "success"
  }




}

