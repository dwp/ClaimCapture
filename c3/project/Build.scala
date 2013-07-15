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
    "com.twitter" % "util-eval_2.10" % "6.3.7" withSources(),
    "com.thoughtworks.xstream" % "xstream" % "1.4.4",
    "com.dwp.carers" % "carersXMLValidation" % "0.4", "postgresql" % "postgresql" % "9.1-901.jdbc4"
  )

  var sO: Seq[Project.Setting[_]] = Seq(scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-Xlint"))

  var sV: Seq[Project.Setting[_]] = Seq(scalaVersion := "2.10.0")

  var sR: Seq[Project.Setting[_]] = Seq(resolvers += "Carers repo" at "http://build.3cbeta.co.uk:8080/artifactory/repo/")

  var sTest: Seq[Project.Setting[_]] = Seq()

  print()
  if (System.getProperty("include") != null ){
    sTest = Seq(testOptions in Test += Tests.Argument("include", System.getProperty("include")))
  }

  var gS: Seq[Project.Setting[_]] = Seq(concurrentRestrictions in Global := Seq(Tags.limit(Tags.CPU, 1),Tags.limit(Tags.Network, 10),Tags.limit(Tags.Test, 1)))

  var f: Seq[Project.Setting[_]] = Seq(sbt.Keys.fork in Test := false)

  var appSettings: Seq[Project.Setting[_]] = SassPlugin.sassSettings ++ sV ++ sO ++ sR ++ gS ++ sTest ++ f

  val main = play.Project(appName, appVersion, appDependencies).settings(appSettings: _*)
}
