package com.sml.apigw.protocols

import spray.json.DefaultJsonProtocol

case class SmlUser(dob: Option[String], email: String, id: Option[Int], name: String)

case class SmlUserResponse(meta: Meta, objects: List[SmlUser])

object SmlUserProtocol extends DefaultJsonProtocol {
  implicit val MetaFormat = jsonFormat(Meta, "limit", "next", "offset", "previous", "total_count")
  implicit val SmlUserFormat = jsonFormat(SmlUser, "dob", "email", "id", "name")
  implicit val SmlUserResponseFormat = jsonFormat(SmlUserResponse, "meta", "objects")
}
