package com.sml.services.protocols

import spray.json.DefaultJsonProtocol

/**
 * Created by eranga on 7/25/15.
 */
object AppointmentProtocol {

  case class Appointment(id: String, patient: String)

  object Protocol extends DefaultJsonProtocol {
    implicit val appointmentFormat = jsonFormat2(Appointment)
  }

}
