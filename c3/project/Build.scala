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
    "org.specs2" %% "specs2" % "1.14" % "test" withSources() withJavadoc(),
    "org.mockito" % "mockito-all" % "1.9.5" % "test" withSources() withJavadoc(),
    "com.dwp.carers" % "carersXMLValidation" % "0.13.1",
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    "me.moocar" % "logback-gelf" % "0.9.6p2",
    "com.google.inject" % "guice" % "3.0",
    "com.tzavellas" % "sse-guice" % "0.7.1"
  )

  var sS: Seq[Project.Setting[_]] = Seq(testOptions in Test += Tests.Argument("sequential", "true"))

  var sO: Seq[Project.Setting[_]] = Seq(scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-Xlint", "-language:reflectiveCalls"))

  var sV: Seq[Project.Setting[_]] = Seq(scalaVersion := "2.10.2")

  var sR: Seq[Project.Setting[_]] = Seq(resolvers += "Carers repo" at "http://build.3cbeta.co.uk:8080/artifactory/repo/")

  var sTest: Seq[Project.Setting[_]] = Seq()

  if (System.getProperty("include") != null ) {

    sTest = Seq(testOptions in Test += Tests.Argument("include", System.getProperty("include")))
  }

  if (System.getProperty("exclude") != null ) {
    sTest = Seq(testOptions in Test += Tests.Argument("exclude", System.getProperty("exclude")))
  }

  var jO: Seq[Project.Setting[_]] = Seq(javaOptions in Test += System.getProperty("waitSeconds"))

  var gS: Seq[Project.Setting[_]] = Seq(concurrentRestrictions in Global := Seq(Tags.limit(Tags.CPU, 4), Tags.limit(Tags.Network, 10), Tags.limit(Tags.Test, 4)))

  var f: Seq[Project.Setting[_]] = Seq(sbt.Keys.fork in Test := false)

  var appSettings: Seq[Project.Setting[_]] =  SassPlugin.sassSettings ++ sS ++ sV ++ sO ++ sR ++ gS ++ sTest ++ f ++ jO

  val scope = "test->test;compile->compile"

  val modulesCommonSettings = sO++ sV++ sR

  val common = play
               .Project(appName + "-common",appVersion,appDependencies,path = file("modules/common"))
               .settings(modulesCommonSettings : _*)

  val s1 = play
          .Project(appName + "-s1",appVersion,appDependencies,path = file("modules/sections/s1"))
          .settings(modulesCommonSettings : _*)
          .dependsOn(common % scope)
          .aggregate(common)

  val s2 = play
          .Project(appName + "-s2",appVersion,appDependencies,path = file("modules/sections/s2"))
          .settings(modulesCommonSettings : _*)
          .dependsOn(common % scope)
          .aggregate(common)

  val s3 = play
    .Project(appName + "-s3",appVersion,appDependencies,path = file("modules/sections/s3"))
    .settings(modulesCommonSettings : _*)
    .dependsOn(common % scope)
    .aggregate(common)

  val s4 = play
    .Project(appName + "-s4",appVersion,appDependencies,path = file("modules/sections/s4"))
    .settings(modulesCommonSettings : _*)
    .dependsOn(common % scope)
    .aggregate(common)

  val s5 = play
    .Project(appName + "-s5",appVersion,appDependencies,path = file("modules/sections/s5"))
    .settings(modulesCommonSettings : _*)
    .dependsOn(common % scope)
    .aggregate(common)

  val s6 = play
    .Project(appName + "-s6",appVersion,appDependencies,path = file("modules/sections/s6"))
    .settings(modulesCommonSettings : _*)
    .dependsOn(common % scope)
    .aggregate(common)

  val s7 = play
    .Project(appName + "-s7",appVersion,appDependencies,path = file("modules/sections/s7"))
    .settings(modulesCommonSettings : _*)
    .dependsOn(common % scope)
    .aggregate(common)

  val s8 = play
    .Project(appName + "-s8",appVersion,appDependencies,path = file("modules/sections/s8"))
    .settings(modulesCommonSettings : _*)
    .dependsOn(common % scope)
    .aggregate(common)

  val s9 = play
    .Project(appName + "-s9",appVersion,appDependencies,path = file("modules/sections/s9"))
    .settings(modulesCommonSettings : _*)
    .dependsOn(common % scope)
    .aggregate(common)

  val s10 = play
    .Project(appName + "-s10",appVersion,appDependencies,path = file("modules/sections/s10"))
    .settings(modulesCommonSettings : _*)
    .dependsOn(common % scope)
    .aggregate(common)

  val s11 = play
    .Project(appName + "-s11",appVersion,appDependencies,path = file("modules/sections/s11"))
    .settings(modulesCommonSettings : _*)
    .dependsOn(common % scope)
    .aggregate(common)

  val circs = play
            .Project(appName + "-circs",appVersion,appDependencies,path = file("modules/circs"))
            .settings(modulesCommonSettings : _*)
            .dependsOn(common % scope)
            .aggregate(common)

  val main = play
             .Project(appName, appVersion, appDependencies)
             .settings(appSettings: _*)
             .dependsOn(common % scope, s1 % scope, s2 % scope,
                        s3 % scope, s4 % scope, s5 % scope, s6 % scope,
                        s7 % scope, s8 % scope, s9 % scope, s10 % scope, s11 % scope,
                        circs % scope)
             .aggregate(common, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, circs)
}