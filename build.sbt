sbtPlugin := true

name := "sbt-webpack"

organization := "com.hindsightsoftware.sbt"

version := "0.1.0"

scalaVersion := "2.10.4"

resolvers += "Typesafe Releases Repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.sbt" % "sbt-js-engine" % "1.0.2")
