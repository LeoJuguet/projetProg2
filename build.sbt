import scala.scalanative.build.*

name := "project"

scalaVersion := "3.2.2"
version := "0.1.0"

enablePlugins(ScalaNativePlugin)

// Package images
nativeLinkStubs := true

githubSuppressPublicationWarning := true
githubTokenSource := TokenSource.GitConfig("github.token") || TokenSource.Environment("GITHUB_TOKEN")
resolvers += Resolver.githubPackages("lafeychine")
libraryDependencies += "io.github.lafeychine" %%% "scala-native-sfml" % "0.5.0"
