package com.sml.apigw.protocols

import spray.json.DefaultJsonProtocol

case class User(id: String, username: String, password: String, role: String)

object UserProtocol extends DefaultJsonProtocol {
  implicit val userFormat = jsonFormat4(User)
}

