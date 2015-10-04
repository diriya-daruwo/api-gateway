package com.sml.apigw.services

import akka.actor.{Actor, ReceiveTimeout}
import com.sml.apigw.protocols.{User, UserResponse}
import org.slf4j.LoggerFactory
import spray.client.UnsuccessfulResponseException
import spray.client.pipelining._
import spray.http._
import spray.routing.RequestContext
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import spray.httpx.SprayJsonSupport._

case class GetUsers()

case class GetUser(id: Int)

case class CreateUser(user: User)

/**
 * Actor class which deals with user service
 * service endpoint: api/v1/users
 */
class UserService(requestContext: RequestContext) extends Actor {

  implicit val system = context.system

  import system.dispatcher

  val logger = LoggerFactory.getLogger(this.getClass().getName())
  context.setReceiveTimeout(30 seconds)

  override def receive = {
    case GetUsers() =>
      logger.debug("Received request for all users")
      context.setReceiveTimeout(60 seconds)
      getUsers()
    case GetUser(id) =>
      logger.debug(s"Received request for user $id")
      context.setReceiveTimeout(60 seconds)
      getUser(id)
    case CreateUser(user) =>
      logger.debug("Received request for create user")
      context.setReceiveTimeout(60 seconds)
      createUser(user)
    case ReceiveTimeout =>
      context.setReceiveTimeout(Duration.Undefined)
      logger.error("Timed out")
    case msg =>
      logger.error("Invalid message received", msg)
  }

  def getUsers() = {
    import com.sml.apigw.protocols.UserProtocol._

    val pipeline = sendReceive ~> unmarshal[UserResponse]

    val response: Future[UserResponse] = pipeline {
      Get("http://10.2.2.132:9000/api/v1/users/?format=json")
    }

    response.onComplete {
      case Success(userResponse) =>
        logger.debug(userResponse.toString)
        requestContext.complete(StatusCodes.Created -> userResponse)
      case Failure(e) =>
        logger.error(e.toString)
        requestContext.complete(e)
    }
  }

  def getUser(id: Int) = {
    import com.sml.apigw.protocols.UserProtocol._

    val pipeline = sendReceive ~> unmarshal[User]

    val response: Future[User] = pipeline {
      Get(s"http://10.2.2.132:9000/api/v1/users/$id/?format=json")
    }

    response.onComplete {
      case Success(smlUser) =>
        logger.debug(smlUser.toString)
        requestContext.complete(StatusCodes.Created -> smlUser)
      case Failure(e) => e match {
        case ure: UnsuccessfulResponseException =>
          if (ure.responseStatus == StatusCodes.MethodNotAllowed)
            requestContext.complete(e)
          else
            requestContext.complete("Error")
      }
    }
  }

  def createUser(user: User) = {
    import com.sml.apigw.protocols.UserProtocol._

    val pipeline = sendReceive

    val response = pipeline {
      Post("http://10.2.2.132:9000/api/v1/users/", user)
    }

    response.onComplete {
      case Success(_) =>
        logger.debug("user created")
        requestContext.complete(StatusCodes.Created -> "created")
      case Failure(e) =>
        logger.error(e.toString)
        requestContext.complete(e)
    }
  }
}
