package com.sml.apigw.protocols

import spray.http.HttpResponse
import spray.httpx.unmarshalling.{MalformedContent, FromResponseUnmarshaller}
import spray.json.DefaultJsonProtocol
import spray.json.lenses.JsonLenses._

case class Patient(device_id: String, id: String, name: String, reg_id: String)

object PatientProtocol extends DefaultJsonProtocol {
  implicit val userStatusUnmarshaller = new FromResponseUnmarshaller[Patient] {
    implicit val userStatusJsonFormat = jsonFormat4(Patient)
    def apply(response: HttpResponse) = try {
      Right(response.entity.asString.extract[Patient]('payload / 'statuses))
    } catch { case x: Throwable =>
      Left(MalformedContent("Could not unmarshal user status.", x))
    }
  }
}
