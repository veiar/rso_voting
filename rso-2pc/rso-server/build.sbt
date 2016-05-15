name := "rso-server"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.0",
  "com.typesafe.akka" %% "akka-actor" % "2.3.4",
  "com.typesafe" % "config" % "1.3.0",
  "org.json4s" %% "json4s-jackson" % "3.3.0",
  "org.mongodb.scala" %% "mongo-scala-driver" % "1.1.0",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3",
  "ch.qos.logback" %  "logback-classic" % "1.1.7",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0")

lazy val common = RootProject(file("../common"))
lazy val project = Project(id = "rso-server", base = file("./")) dependsOn (common)

