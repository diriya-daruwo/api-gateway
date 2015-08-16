package com.sml.apigw.etcd

import spray.json._

object EtcdJsonProtocol extends DefaultJsonProtocol {

  //single key/values
  case class NodeResponse(key: String, value: Option[String], modifiedIndex: Int, createdIndex: Int, ttl: Option[Int])

  case class EtcdResponse(action: String, node: NodeResponse, prevNode: Option[NodeResponse])

  //for handling error messages
  case class Error(errorCode: Int, message: String, cause: String, index: Int)

  implicit val nodeResponseFormat = jsonFormat5(NodeResponse)
  implicit val etcdResponseFormat = jsonFormat3(EtcdResponse)

  implicit val errorFormat = jsonFormat4(Error)
}
