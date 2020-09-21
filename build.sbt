val dottyVersion = "0.27.0-RC1"
val Http4sVersion = "1.0.0-M4"
val CirceVersion = "0.13.0"
val LogbackVersion = "1.2.3"

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / useSuperShell := false
ThisBuild / scalaVersion := dottyVersion

ThisBuild / run / fork := true

lazy val root = project
  .in(file("."))
  .settings(
    name := "dotty-simple",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      ("org.http4s"     %% "http4s-ember-server" % Http4sVersion).withDottyCompat(scalaVersion.value),
      ("io.circe"       %% "circe-generic"       % CirceVersion).withDottyCompat(scalaVersion.value),
      ("org.http4s"     %% "http4s-circe"        % Http4sVersion).withDottyCompat(scalaVersion.value),
      ("org.http4s"     %% "http4s-dsl"          % Http4sVersion).withDottyCompat(scalaVersion.value),
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
      "org.scalameta"   %% "munit"               % "0.7.12" % Test,
      "com.novocode"    %  "junit-interface"     % "0.11" % Test,
      ),
    scalacOptions += "-language:implicitConversions",
    testFrameworks += new TestFramework("munit.Framework")
  )
