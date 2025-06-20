import sbt._

object Dependencies
{
  val ScalaVersion = "2.13.16"
  val Http4sVersion = "0.23.30"
  val CirceVersion = "0.14.14"

  lazy val http4sDsl = "org.http4s" %% "http4s-dsl" % Http4sVersion

  lazy val http4sEmberServer = "org.http4s" %% "http4s-ember-server" % Http4sVersion

  lazy val http4sCirce = "org.http4s" %% "http4s-circe" % Http4sVersion

  lazy val http4sEmberClient = "org.http4s" %% "http4s-ember-client" % Http4sVersion

  lazy val circeGeneric = "io.circe" %% "circe-generic" % CirceVersion

  lazy val circeParser = "io.circe" %% "circe-parser" % CirceVersion

  lazy val circeLiteral = "io.circe" %% "circe-literal" % CirceVersion

  lazy val jodaTime = "joda-time" % "joda-time" % "2.14.0"

  lazy val pureconfig = "com.github.pureconfig" %% "pureconfig" % "0.17.9"

  lazy val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.5.18"

  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"

  lazy val kindProjector = "org.typelevel" %% "kind-projector" % "0.13.3" cross CrossVersion.full

  lazy val betterMonadicFor = "com.olegpy" %% "better-monadic-for" % "0.3.1"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.19"

  lazy val scalaMock = "org.scalamock" %% "scalamock" % "7.3.2"

  lazy val pegdown = "org.pegdown" % "pegdown" % "1.6.0"
}
