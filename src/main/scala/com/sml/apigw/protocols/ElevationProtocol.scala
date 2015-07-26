package com.sml.apigw.protocols

import spray.json.{DefaultJsonProtocol, JsonFormat}

case class Location(lat: Double, lng: Double)

case class Elevation(location: Location, elevation: Double)

case class GoogleElevationApiResult[T](status: String, results: List[T])

object ElevationProtocol extends DefaultJsonProtocol {
  implicit val locationFormat = jsonFormat2(Location)
  implicit val elevationFormat = jsonFormat2(Elevation)

  implicit def googleElevationApiResultFormat[T: JsonFormat] = jsonFormat2(GoogleElevationApiResult.apply[T])
}
