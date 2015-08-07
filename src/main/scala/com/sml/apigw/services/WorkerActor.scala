package com.sml.apigw.services

import akka.actor.{Actor, ActorSystem, Props, ActorLogging}
import akka.io.IO
import akka.routing._
import com.sml.apigw.protocols.{Prescription, Appointment}


object WorkerActor {

  case class GetAppointment()

  case class GetPrescriptions()

}

class WorkerActor extends Actor with ActorLogging {

  import WorkerActor._

  def receive = {
    case GetAppointment =>
      sender ! Appointment("34", "era")
    case GetPrescriptions =>
      sender ! Prescription("ere", "34ewerewrewr", "era")
  }
}

