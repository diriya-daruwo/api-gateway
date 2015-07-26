package com.sml.services.auth

import spray.routing.authentication.{BasicAuth, UserPass}
import spray.routing.directives.AuthMagnet

import scala.concurrent._

case class User(username: String, hashedPassword: String) {
  def isAuthenticatedUser(password: String): Boolean = password.equalsIgnoreCase(hashedPassword)
}

case class AuthInfo(user: User) {
  def hasPermission(permission: String) = true
}

trait BasicAuthenticator {
  def basicAuthenticator(implicit executor: ExecutionContext): AuthMagnet[User] = {
    def authenticator(userPass: Option[UserPass]): Future[Option[User]] = {
      Future {
        // TODO communicate with auth service and authenticate user
        // TODO database query or any other option to find matching user
        if (userPass.exists(up => up.user == "john" && up.pass == "123")) Some(User(username = "eranga", hashedPassword = "123"))
        else None
      }
    }

    BasicAuth(authenticator _, realm = "Private API")
  }
}

