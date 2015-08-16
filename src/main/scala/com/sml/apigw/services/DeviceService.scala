package com.sml.apigw.services

import akka.actor.{ActorSystem, Actor, ReceiveTimeout}
import com.sml.apigw.etcd.EtcdClient
import com.sml.apigw.etcd.EtcdJsonProtocol.EtcdResponse
import com.sml.apigw.protocols.DeviceResponse
import org.slf4j.LoggerFactory
import spray.client.pipelining._
import spray.http._
import spray.routing.HttpService
import spray.routing.RequestContext
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import spray.httpx.SprayJsonSupport._

class DeviceService(requestContext: RequestContext) extends Actor {


  implicit val system = context.system

  import system.dispatcher

  val logger = LoggerFactory.getLogger(this.getClass().getName())
  context.setReceiveTimeout(30 seconds)

  def receive = {
    case "GET" =>
      logger.debug("Received request for devices")
      context.setReceiveTimeout(60 seconds)
      findDeviceService()
      getDevices()
    case ReceiveTimeout =>
      context.setReceiveTimeout(Duration.Undefined)
      logger.error("Timed out")
    case _ =>
      logger.error("I did not understand the message that you sent!")
  }

  def getDevices() = {
    import com.sml.apigw.protocols.DeviceProtocol._
    val pipeline = (
      addHeader("Authorization", "Basic YWRtaW46YWRtaW4=")
        ~> sendReceive
        ~> unmarshal[DeviceResponse]
      )

    val response: Future[DeviceResponse] = pipeline {
      Get("http://10.2.2.132:8080/api/v1/devices/?format=json") ~> addCredentials(BasicHttpCredentials("eranga", "123"))
    }

    response.onComplete {
      case Success(deviceResponse) =>
        logger.debug(deviceResponse.toString)
        requestContext.complete(StatusCodes.Created -> deviceResponse)
      case Failure(e) =>
        logger.error(e.toString)
        requestContext.complete(e)
    }
  }

  def findDeviceService() = {
    val system = ActorSystem("etcd")
    val client = new EtcdClient("http://192.168.59.103:2379")

    client.setKey("configKey", "configValue")

    val response: Future[EtcdResponse] = client.getKey("configKey")

    response onComplete {
      case Success(response: EtcdResponse) =>
        System.out.println(response)
        system.shutdown()
      case Failure(error) =>
        System.out.println(error)
        system.shutdown()
    }
  }
}
