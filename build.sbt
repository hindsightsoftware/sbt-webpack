import bintray.Keys._

sbtPlugin := true

name := "sbt-webpack"

organization := "com.hindsightsoftware.sbt"

version := "0.2.0"

scalaVersion := "2.10.4"

resolvers += "Typesafe Releases Repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.sbt" % "sbt-js-engine" % "1.0.2")

publishMavenStyle := false

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

bintrayPublishSettings

repository in bintray := "sbt-plugins"

bintrayOrganization in bintray := Some("hindsightsoftware")