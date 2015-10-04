package com.sml.apigw.protocols

import spray.json.DefaultJsonProtocol

case class Appointment(id: Option[String], patient: String, doctor: String, date: String)

object AppointmentProtocol extends DefaultJsonProtocol {
  implicit val AppointmentFormat = jsonFormat(Appointment, "id", "patient", "doctor", "date")
}

