package com.sml.apigw.protocols

import spray.json.DefaultJsonProtocol

case class User(dob: Option[String], email: String, id: Option[Int], name: String)

case class UserResponse(meta: Meta, objects: List[User])

object UserProtocol extends DefaultJsonProtocol {
  implicit val MetaFormat = jsonFormat(Meta, "limit", "next", "offset", "previous", "total_count")
  implicit val UserFormat = jsonFormat(User, "dob", "email", "id", "name")
  implicit val UserResponseFormat = jsonFormat(UserResponse, "meta", "objects")
}
