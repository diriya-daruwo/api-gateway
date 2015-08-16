package com.sml.apigw.protocols

import spray.json.DefaultJsonProtocol

case class Meta(limit: Int, next: Option[String], offset: Int, previous: Option[String], total_count: Int)
case class Device(device_id: String, id: String, name: String, reg_id: String)
case class DeviceResponse(meta: Meta, objects: List[Device])

object DeviceProtocol extends DefaultJsonProtocol {
  implicit val MetaFormat = jsonFormat(Meta, "limit", "next", "offset", "previous", "total_count")
  implicit val DeviceFormat = jsonFormat(Device, "device_id", "id", "name", "reg_id")
  implicit val DeviceResponseFormat = jsonFormat(DeviceResponse, "meta", "objects")
}
