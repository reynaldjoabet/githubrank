
// The simplest possible sbt build file is just one line:

scalaVersion := "2.13.3"

version := "1.0"

val Http4s="0.23.7"
val Circe="0.14.1"
val Logback= "1.2.10"
ThisBuild / scalaVersion := "2.13.7"

lazy val root = (project in file("."))
  .settings(
    name := "githubrank",
    libraryDependencies ++=Seq(
      "org.http4s" %% "http4s-blaze-server" % Http4s,
      "org.http4s" %% "http4s-circe" % Http4s,
      "org.http4s" %% "http4s-dsl" % Http4s,
      "org.http4s" %% "http4s-blaze-client" % Http4s,
      "ch.qos.logback" % "logback-classic" % Logback,
      "io.circe" %% "circe-generic" % Circe,
    
    )

  )
