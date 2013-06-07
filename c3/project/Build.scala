import _root_.sbt.Keys._
import sbt._
import sbt.Keys._
import play.Project._
import net.litola.SassPlugin

object ApplicationBuild extends Build {

  val appName         = "c3"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm
  )

  var sO: Seq[Project.Setting[_]] = Seq(scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-Xlint"))

  var sV: Seq[Project.Setting[_]] = Seq(scalaVersion := "2.10.0")

  var appSettings: Seq[Project.Setting[_]] = SassPlugin.sassSettings ++ sV ++ sO

  val main = play.Project(appName, appVersion, appDependencies).settings(appSettings: _*)
}
