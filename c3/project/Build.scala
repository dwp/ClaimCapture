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
    anorm,
    "org.mockito" % "mockito-all" % "1.9.5" % "test" withSources() withJavadoc(),
  "com.dwp.carers" % "carersXMLValidation" % "0.2"
  )

  var sO: Seq[Project.Setting[_]] = Seq(scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-Xlint"))

  var sV: Seq[Project.Setting[_]] = Seq(scalaVersion := "2.10.0")

  var sR: Seq[Project.Setting[_]] = Seq(resolvers += "Carers repo" at "http://build.3cbeta.co.uk:8080/artifactory/repo/")

  var appSettings: Seq[Project.Setting[_]] = SassPlugin.sassSettings ++ sV ++ sO ++ sR

  val main = play.Project(appName, appVersion, appDependencies).settings(appSettings: _*)
}
