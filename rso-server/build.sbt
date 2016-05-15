name := "rso-server"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.0",
  "com.typesafe.akka" %% "akka-actor" % "2.3.4",
  "com.typesafe" % "config" % "1.3.0",
  "org.json4s" %% "json4s-jackson" % "3.3.0",
  "org.mongodb.scala" %% "mongo-scala-driver" % "1.1.0",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3")

//mainClass in (Compile,run) := Some("com.mac.rso.Main")

