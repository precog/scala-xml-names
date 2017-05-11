import sbt._, Keys._

import slamdata.CommonDependencies
import slamdata.SbtSlamData.transferPublishAndTagResources

lazy val baseSettings = commonBuildSettings ++ Seq(
  organization := "com.slamdata",
  libraryDependencies += CommonDependencies.slamdata.predef,
  wartremoverWarnings in (Compile, compile) --= Seq(
    // Triggered by Monocle @Lenses annotation.
    Wart.PublicInference
  )
)

lazy val publishSettings = commonPublishSettings ++ Seq(
  organizationName := "SlamData Inc.",
  organizationHomepage := Some(url("http://slamdata.com")),
  homepage := Some(url("https://github.com/slamdata/scala-xml-names")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/slamdata/scala-xml-names"),
      "scm:git@github.com:slamdata/scala-xml-names.git"
    )
  ))

lazy val allSettings =
  baseSettings ++ publishSettings

lazy val root = (project in file("."))
  .aggregate(core, scalacheck)
  .settings(allSettings)
  .settings(noPublishSettings)
  .settings(transferPublishAndTagResources)
  .settings(Seq(
    name := "xml-names"
  ))

lazy val core = (project in file("core"))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(allSettings)
  .settings(Seq(
    name := "xml-names-core",
    libraryDependencies ++= Seq(
      CommonDependencies.monocle.core,
      CommonDependencies.monocle.`macro`,
      CommonDependencies.refined.refined,
      CommonDependencies.scalaz.core
    )
  ))

lazy val scalacheck = (project in file("scalacheck"))
  .dependsOn(core)
  .enablePlugins(AutomateHeaderPlugin)
  .settings(allSettings)
  .settings(Seq(
    name := "xml-names-scalacheck",
    libraryDependencies ++= Seq(
      CommonDependencies.scalacheck.scalacheck
    )
  ))
