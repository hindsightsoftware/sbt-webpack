package com.hindsightsoftware.sbt.webpack

import com.typesafe.sbt.jse.JsTaskImport.JsTaskKeys.timeoutPerSource
import com.typesafe.sbt.jse.SbtJsEngine.autoImport.JsEngineKeys.{command, engineType}
import com.typesafe.sbt.jse.SbtJsTask
import com.typesafe.sbt.web.Import.WebKeys.{nodeModuleDirectories, webTarget}
import com.typesafe.sbt.web.SbtWeb
import com.typesafe.sbt.web.SbtWeb.autoImport.{Plugin, WebKeys}
import com.typesafe.sbt.web.pipeline.Pipeline
import sbt.Keys.{baseDirectory, resourceManaged, state, streams}
import sbt._

object WebpackImport {
  val webpack = TaskKey[Pipeline.Stage]("webpack", "Run webpack as a asset pipeline task")
}

object SbtWebpack extends AutoPlugin {

  override def requires = SbtWeb

  override def trigger = AllRequirements

  val autoImport = WebpackImport

  import com.hindsightsoftware.sbt.webpack.WebpackImport._

  override def projectSettings = Seq(
    resourceManaged in webpack := webTarget.value / webpack.key.label,
    webpack := webpackPipelineTask.dependsOn(WebKeys.nodeModules in Plugin).value
  )

  def webpackPipelineTask: Def.Initialize[Task[Pipeline.Stage]] = Def.task {
    mappings =>
      val webpackConfigFile = baseDirectory.value / "webpack.config.js"
      val webpackjsShell = baseDirectory.value / "node_modules" / "webpack" / "bin" / "webpack.js"
      val outputDir = (resourceManaged in webpack).value

      streams.value.log.info("Building assets with Webpack")

      SbtWeb.syncMappings(
        streams.value.cacheDirectory,
        mappings,
        outputDir
      )

      val cacheDirectory = streams.value.cacheDirectory / webpack.key.label

      val runUpdate = FileFunction.cached(cacheDirectory, FilesInfo.hash) {
        _ =>
          SbtJsTask.executeJs(
            state.value,
            (engineType in webpack).value,
            (command in webpack).value,
            (nodeModuleDirectories in Plugin).value.map(_.getPath),
            webpackjsShell,
            Seq("--output-path", outputDir.getAbsolutePath, "--config", webpackConfigFile.getAbsolutePath),
            (timeoutPerSource in webpack).value * mappings.size)

          outputDir.***.get.toSet
      }

      runUpdate(outputDir.***.get.toSet).filter(_.isFile).pair(relativeTo(outputDir))
  }

}
