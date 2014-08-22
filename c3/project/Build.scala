import sbt._
import sbt.Keys._
import play.Project._
import net.litola.SassPlugin
import de.johoop.jacoco4sbt.JacocoPlugin._

object ApplicationBuild extends Build {
  val appName         = "c3"

  val appVersion      = "2.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    cache,
    anorm,
    filters,
    "org.specs2"         %% "specs2"              % "2.3.6" % "test" withSources() withJavadoc(),
    "org.mockito"         % "mockito-all"         % "1.9.5" % "test" withSources() withJavadoc(),
    "com.typesafe.akka"  %% "akka-testkit"        % "2.2.3" % "test" withSources() withJavadoc(),
    "com.typesafe.akka"  %% "akka-agent"          % "2.2.3" % "test" withSources() withJavadoc(),
    "com.typesafe.akka"  %% "akka-actor"          % "2.2.3" % "test" withSources() withJavadoc(),
    "com.typesafe.akka"  %% "akka-remote"         % "2.2.3" % "test" withSources() withJavadoc(),
    "com.dwp.carers"     %% "xmlcommons"          % "2.0.16",
    "com.dwp.carers"     %%  "wscommons"          % "1.0",
    "postgresql"          % "postgresql"          % "9.1-901.jdbc4",
    "com.h2database"      % "h2"                  % "1.3.174",
    "me.moocar"           % "logback-gelf"        % "0.9.6p2",
    "com.github.rjeschke" % "txtmark"             % "0.10",
    "org.jacoco"          % "org.jacoco.core"     % "0.6.4.201312101107",
    "org.jacoco"          % "org.jacoco.report"   % "0.6.4.201312101107",
    "com.dwp"            %% "play2-multimessages" % "2.2.1",
    "com.typesafe"       %% "play-plugins-mailer" % "2.2.0",
    "com.github.mumoshu" %% "play2-memcached"     % "0.4.0",
    "com.codahale.metrics" % "metrics-healthchecks" % "3.0.1",
    "com.google.guava"    % "guava"               % "14.0",
    "org.feijoas"        %% "mango"               % "0.10",
    "org.jasypt"          % "jasypt"              % "1.9.2",
    "com.kenshoo"        %% "metrics-play"        % "0.1.4"
  )

  var sO: Seq[Def.Setting[_]] = Seq(scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-Xlint", "-language:reflectiveCalls"))

  var sV: Seq[Def.Setting[_]] = Seq(scalaVersion := "2.10.3")

  var sR: Seq[Def.Setting[_]] = Seq(
    resolvers += "Carers repo" at "http://build.3cbeta.co.uk:8080/artifactory/repo/",
    resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases")

  var sTest: Seq[Def.Setting[_]] = Seq()

  if (System.getProperty("include") != null ) {

    sTest = Seq(testOptions in Test += Tests.Argument("include", System.getProperty("include")))
  }

  if (System.getProperty("exclude") != null ) {
    sTest = Seq(testOptions in Test += Tests.Argument("exclude", System.getProperty("exclude")))
  }

  var jO: Seq[Def.Setting[_]] = Seq(javaOptions in Test += System.getProperty("waitSeconds"),
                                    testOptions in Test += Tests.Argument("sequential", "true"))

  var gS: Seq[Def.Setting[_]] = Seq(concurrentRestrictions in Global := Seq(Tags.limit(Tags.CPU, 4), Tags.limit(Tags.Network, 10), Tags.limit(Tags.Test, 4)))

  var f: Seq[Def.Setting[_]] = Seq(sbt.Keys.fork in Test := false)

  var jcoco: Seq[Def.Setting[_]] = Seq(parallelExecution in jacoco.Config := false)

  val keyStore = System.getProperty("sbt.carers.keystore")

  var keyStoreOptions: Seq[Def.Setting[_]] =  Seq(javaOptions in Test += ("-Dcarers.keystore=" + keyStore))

  var appSettings: Seq[Def.Setting[_]] =  SassPlugin.sassSettings ++ sV ++ sO ++ sR ++ gS ++ sTest ++ jO ++ f ++ jcoco ++ keyStoreOptions

  val main = play.Project(appName, appVersion, appDependencies, settings = play.Project.playScalaSettings ++ jacoco.settings).settings(appSettings: _*)
}
