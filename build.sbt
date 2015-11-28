organization := "com.fitlog"
name := "bodyweightlogservice"
version := "1.0-SNAPSHOT"
scalaVersion := "2.10.2"

resolvers += Resolver.mavenLocal
libraryDependencies ++= Seq (
  "com.webtrends" % "wookiee-core" % "1.1-SNAPSHOT",
  "com.webtrends" % "wookiee-spray" % "1.0-SNAPSHOT"
)
