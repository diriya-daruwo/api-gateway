package com.sml.apigw.protocols

import spray.json.DefaultJsonProtocol

case class Prescription(id: String, patient: String, appointment: String)

object PrescriptionProtocol extends DefaultJsonProtocol {
  implicit val prescriptionFormat = jsonFormat3(Prescription)
}

