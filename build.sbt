name := "toy-search-engine"

version := "1.0"

scalaVersion := "2.12.1"

fork := true

val sprayVersion = "1.3.3"
val akkaVersion = "2.4.16"
val akkaHttpVersion = "10.0.3"
val scalaTestVersion = "3.0.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
)
    