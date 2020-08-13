val dottyVersion = "0.26.0-RC1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "nifty-dotty",
    version := "0.1.0",
    scalaVersion := dottyVersion,
    libraryDependencies += "com.typesafe.akka" % "akka-stream_2.13" % "2.6.8",
    libraryDependencies += "com.typesafe.akka" % "akka-actor-typed_2.13" % "2.6.8"
  )
