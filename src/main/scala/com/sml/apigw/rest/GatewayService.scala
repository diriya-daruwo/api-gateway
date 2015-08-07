package com.sml.apigw.rest

import akka.actor.{Actor, Props}
import akka.event.slf4j.SLF4JLogging
import akka.pattern.ask
import akka.util.Timeout
import com.sml.apigw.protocols.{Appointment, Prescription}
import com.sml.apigw.services.WorkerActor
import com.sml.apigw.services.WorkerActor.{GetPrescriptions, GetAppointment}
import spray.json._
import spray.routing.HttpService

import scala.concurrent.Future
import scala.concurrent.duration._

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
  import com.sml.apigw.protocols.AppointmentProtocol._
  import com.sml.apigw.protocols.PrescriptionProtocol._

  implicit def executionContext = actorRefFactory.dispatcher

  //implicit val timeout = Timeout(5 seconds)
  //val worker = actorRefFactory.actorOf(Props[WorkerActor], "worker")

  val router =
    pathPrefix("api" / "v1") {
      path("appointments") {
        get {
          //val appointmentService = actorRefFactory.actorOf(Props(new AppointmentService(requestContext)))
          //appointmentService ! AppointmentService.Get()
          complete {
            //(worker ? GetAppointment).mapTo[Appointment]
            getAppointments()
          }
        }
      } ~
        path("prescriptions") {
          get {
            //val appointmentService = actorRefFactory.actorOf(Props(new AppointmentService(requestContext)))
            //appointmentService ! AppointmentService.Get()
            //complete {
            //  (worker ? GetPrescriptions).mapTo[Prescription]
            //}
            complete {
              getPrescriptions()
            }
          }
        }
    }

  //  def getAppointments(): Future[Appointment] = {
  //    val f: Future[App] = Future {
  //      new Appointment("3", "3")
  //    }
  //  }

  def getAppointments() = Future[Appointment] {
    // TODO call for appointment service
    new Appointment("1", "2")
  }

  def getPrescriptions() = Future[Prescription] {
    // TODO call for prescription service
    new Prescription("1", "2", "34")
  }
}

