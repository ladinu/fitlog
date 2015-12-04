organization := "com.fitlog"
name := "bodyweightlogservice"
version := "1.0-SNAPSHOT"
scalaVersion := "2.11.7"

resolvers += Resolver.sonatypeRepo("releases")
resolvers += "Jfrog OSS" at "http://oss.jfrog.org/artifactory/oss-release-local"

libraryDependencies ++= Seq (
  "com.webtrends" % "wookiee-core" % "1.1.4",
  "com.webtrends" % "wookiee-spray" % "1.0.4",
  "com.typesafe.slick" %% "slick" % "3.1.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4"
)
