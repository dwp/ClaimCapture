import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "playParent"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm
  )
  val mainDeps = Seq()
lazy val playChild = play.Project(appName + "-playChild", appVersion, appDependencies, path = file("playChild")).settings(scalaVersion := "2.10.0")

  lazy  val main = play.Project(appName, appVersion, appDependencies).settings(
      scalaVersion := "2.10.0" // Add your own project settings here      
    ).dependsOn(playChild).aggregate(playChild)



}
