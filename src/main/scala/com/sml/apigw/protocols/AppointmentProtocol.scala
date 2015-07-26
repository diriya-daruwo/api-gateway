package com.sml.apigw.protocols

import spray.json.DefaultJsonProtocol

case class Appointment(id: String, patient: String)

object AppointmentProtocol extends DefaultJsonProtocol {
  implicit val appointmentFormat = jsonFormat2(Appointment)
}

