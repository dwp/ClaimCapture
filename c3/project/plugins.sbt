// Comment to get more information during initialization
logLevel := Level.Warn

resolvers += "Carers repo2" at s"${System.getProperty("artifactory_url")!=null match { case true => s"${System.getProperty("artifactory_url")}/repo"; case false => "http://build.3cbeta.co.uk:8080/artifactory/repo/";}}"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.3")

addSbtPlugin("de.johoop" % "jacoco4sbt" % "2.1.6")

addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.0")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.4")

addSbtPlugin("default" % "sbt-sass" % "0.1.9")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-play-enhancer" % "1.1.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.6")

libraryDependencies += "gov.dwp.carers" %% "carerscommon" % "7.17-SNAPSHOT"

//if required provide credentials file location
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
