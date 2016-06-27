
lazy val root = (project in file(".")).enablePlugins(SbtWeb)

JsEngineKeys.engineType := JsEngineKeys.EngineType.Node

resolvers += "Typesafe Releases Repository" at "http://repo.typesafe.com/typesafe/releases/"

pipelineStages := Seq(webpack)

webpackConfig := baseDirectory.value / "production-webpack.config.js"
