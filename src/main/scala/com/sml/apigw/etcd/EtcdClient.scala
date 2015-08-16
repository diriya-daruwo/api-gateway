package com.sml.apigw.etcd

import akka.actor.ActorRefFactory
import spray.client.pipelining._
import spray.http._
import spray.httpx.SprayJsonSupport._

import scala.concurrent.Future
import scala.concurrent.duration._

object EtcdClient {
  def apply(conn: String = "http://localhost:4001")(implicit system: ActorRefFactory) = {
    new EtcdClient(conn)(system)
  }
}

class EtcdClient(conn: String)(implicit val system: ActorRefFactory) {
  private val baseUrl = s"$conn/v2/keys"

  import system.dispatcher
  import EtcdJsonProtocol._

  def getKey(key: String): Future[EtcdResponse] = {
    getKeyAndWait(key, wait = false)
  }

  def getKeyAndWait(key: String, wait: Boolean = true): Future[EtcdResponse] = {
    defaultPipeline(Get(s"$baseUrl/$key?wait=$wait"))
  }

  def setKey(key: String, value: String, ttl: Option[Duration] = None): Future[EtcdResponse] = {
    val q: Map[String, String] = Map("value" -> value, "ttl" -> ttl.map(_.toSeconds.toString).getOrElse(""))
    defaultPipeline(Put(Uri(s"$baseUrl/$key").withQuery(q)))
  }

  private val defaultPipeline: HttpRequest => Future[EtcdResponse] = (
    sendReceive
      ~> unmarshal[EtcdResponse]
    )
}
