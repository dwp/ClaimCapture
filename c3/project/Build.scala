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
    "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  )

  var appSettings : Seq[Project.Setting[_]] = Seq(testOptions in Test := Nil) ++ Seq(SassPlugin.sassOptions := Seq("--compass"))

  val main = play.Project(appName, appVersion, appDependencies).settings(
    testOptions in Test := Nil
  )

}
