import scala.scalanative.build.*

name := "project"

scalaVersion := "3.2.1"
version := "0.1.0"

enablePlugins(ScalaNativePlugin)

githubSuppressPublicationWarning := true
githubTokenSource := TokenSource.GitConfig("github.token")
resolvers += Resolver.githubPackages("lafeychine")
libraryDependencies += "io.github.lafeychine" %%% "scala-native-sfml" % "0.2.4"
