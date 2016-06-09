import com.earldouglas.xwp.JettyPlugin
import org.scalatra.sbt._
import sbt.Keys._
import sbt._

object RsoClientBuild extends Build {
  val Organization = "com.mac"
  val Name = "rso-client"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.8"
  val ScalatraVersion = "2.4.0"

  lazy val common = RootProject(file("../common"))

  lazy val project = Project(
    "rso-client",
    file("."),
    settings = ScalatraPlugin.scalatraSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "org.scalatra" %% "scalatra-json" % ScalatraVersion,
        "com.typesafe.akka" %% "akka-actor" % "2.3.4",
        "net.databinder.dispatch" %% "dispatch-core" % "0.11.1",
        "org.mongodb.scala" %% "mongo-scala-driver" % "1.1.0",
        "org.json4s" %% "json4s-jackson" % "3.3.0",
        "org.json4s" %% "json4s-native" % "3.3.0",
        "ch.qos.logback" % "logback-core" % "1.0.9",
        "ch.qos.logback" % "logback-classic" % "1.0.9",
        "org.slf4j" % "slf4j-api" % "1.7.19",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0",
        "org.eclipse.jetty" % "jetty-webapp" % "9.2.15.v20160210" % "container;compile",
        "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
        "com.typesafe" % "config" % "1.3.0",
        "com.chuusai" %% "shapeless" % "2.3.1"
      ),
      javaOptions ++= Seq(
        "-Xdebug",
        "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
      )
    )
  ).enablePlugins(JettyPlugin).dependsOn(common)
}