package com.sml.apigw.protocols

import spray.json.DefaultJsonProtocol

case class Appointment(id: Option[String], patient: User, doctor: User, date: String)

object AppointmentProtocol extends DefaultJsonProtocol {
  implicit val UserFormat = jsonFormat(User, "dob", "email", "id", "name")
  implicit val AppointmentFormat = jsonFormat(Appointment, "id", "patient", "doctor", "date")
}

