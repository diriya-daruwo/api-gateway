name := "api-gw"

version := "0.1"

scalaVersion := "2.11.5"

libraryDependencies ++= {
  val akkaVersion       = "2.3.9"
  val sprayVersion      = "1.3.2"
  Seq(
    "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
    "io.spray"          %% "spray-can"       % sprayVersion,
    "io.spray"          %% "spray-routing"   % sprayVersion,
    "io.spray"          %% "spray-client"    % sprayVersion,
    "io.spray"          %% "spray-json"      % sprayVersion,
    "com.typesafe.akka" %% "akka-slf4j"      % akkaVersion,
    "net.virtual-void"  %% "json-lenses"     % "0.6.0"
  )
}

resolvers ++= Seq(
  "Spray repository" at "http://repo.spray.io",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)


