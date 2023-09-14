ThisBuild / tlBaseVersion := "1.6"

ThisBuild / developers := List(tlGitHubDev("kamil-adam", "Kamil Adam"))

ThisBuild / crossScalaVersions := Seq("3.3.0")
ThisBuild / tlVersionIntroduced := Map("3" -> "1.1.5")

ThisBuild / licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))
ThisBuild / startYear := Some(2023)

ThisBuild / tlJdkRelease := Some(8)

ThisBuild / tlSonatypeUseLegacyHost := false
ThisBuild / tlCiReleaseTags := false

val catsVersion = "2.9.0"
val catsEffectVersion = "3.5.1"
val catsMtlVersion = "1.3.1"
val fs2Version = "3.8.0"
//val spec2Version = "4.20.2"
val spec2Version = "4.19.2"
val jawnVersion = "1.5.1"
val scalaTestVersion = "3.2.16"

val coreJavaOptions = Seq("-Xmx4g", "-XX:+UseG1GC")

lazy val coreSettings = Seq(
  name := "catculus",
  moduleName := "catculus-core",
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-kernel" % catsVersion,
    "org.typelevel" %%% "cats-core" % catsVersion,
    "org.typelevel" %%% "cats-laws" % catsVersion,
    "org.typelevel" %%% "cats-free" % catsVersion,
    "org.typelevel" %%% "cats-testkit" % catsVersion,
    "org.typelevel" %%% "algebra" % catsVersion,
  ),
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-effect" % catsEffectVersion,
    "org.typelevel" %%% "cats-effect-kernel" % catsEffectVersion,
    "org.typelevel" %%% "cats-effect-laws" % catsEffectVersion % Test,
  ),
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-mtl" % catsMtlVersion,
    "org.typelevel" %%% "cats-mtl-laws" % catsMtlVersion % Test,
  ),
  libraryDependencies ++= Seq(
    "co.fs2" %%% "fs2-core" % fs2Version,
    "co.fs2" %%% "fs2-scodec" % fs2Version,
    "co.fs2" %%% "fs2-io" % fs2Version,
  ),
  //  libraryDependencies ++= Seq(
  //    "org.typelevel" %%% "jawn-parser" % jawnVersion,
  //    "org.typelevel" %%% "jawn-ast" % jawnVersion,
  //  ),
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "mouse" % "1.2.1",
    "org.typelevel" %%% "kittens" % "3.0.0",
    "org.typelevel" %%% "cats-collections-core" % "0.9.7",
  ),
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "discipline-core" % "1.5.1",
    "org.typelevel" %%% "discipline-scalatest" % "2.2.0" % Test,
    "org.typelevel" %%% "discipline-specs2" % "1.4.0" % Test,
    //    "org.typelevel" %%% "discipline-munit" % "1.0.9" % Test,
  ),
  libraryDependencies ++= Seq(
    //    "org.scalatest" %%% "scalatest" % scalaTestVersion % Test,
    "org.scalatest" %%% "scalatest-funspec" % scalaTestVersion % Test,
    "org.scalatest" %%% "scalatest-wordspec" % scalaTestVersion % Test,
  ),
  libraryDependencies ++= Seq(
    "org.specs2" %%% "specs2-core" % spec2Version % Test,
    "org.specs2" %%% "specs2-scalacheck" % spec2Version % Test,
  ),
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "spire" % "0.18.0",
    "org.typelevel" %%% "cats-parse" % "0.3.10",
    "org.typelevel" %%% "log4cats-core" % "2.6.0",
    "org.scalacheck" %%% "scalacheck" % "1.17.0" % Test,

    //    "org.scalameta" %%% "munit" % "1.0.0" % Test,
    //    "org.scalameta" %%% "munit-scalacheck" % "0.7.29" % Test,
  ),
  Test / javaOptions ++= coreJavaOptions,
)

val coreJvmSettings = Seq(
  Test / fork := true,
  Test / javaOptions ++= coreJavaOptions,
)

val coreJsSettings = Seq(
  tlVersionIntroduced ~= {
    _ ++ List("2.13").map(_ -> "1.0.2").toMap
  },
  scalaJSLinkerConfig ~= {
    _
      .withModuleKind(ModuleKind.ESModule)
//      .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("livechart")))
  },

)

val coreNativeSettings = Seq(
  tlVersionIntroduced := Map(
    "2.13" -> "1.1.3",
    "3" -> "1.5.0"
  ),
  Test / javaOptions ++= coreJavaOptions,
)

val core =
  crossProject(NativePlatform, JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("catculator-core"))
    .settings(coreSettings)
    .jvmSettings(coreJvmSettings)
    .jsSettings(coreJsSettings)
    .nativeSettings(coreNativeSettings)

val rootSettings = Seq(
  name := "catculus",
)

val root =
  tlCrossRootProject
    .aggregate(core)
    .settings(rootSettings)

addCommandAlias("scalafixWTF", "scalafixEnable; scalafixAll")
addCommandAlias("scalafmtWTF", "scalafmtSbt; scalafmtAll")
addCommandAlias("compileAll", "clean; compile; Test/compile")
addCommandAlias("testAll", "coreJS/test; coreNative/test")
addCommandAlias("coverageAll", "coverage; coreJVM/test")
addCommandAlias("all", "scalafixWTF; scalafmtAll; compileAll; testAll; coverageAll; coverageReport")
//addCommandAlias("all", "scalafmtAll; compileAll; testAll; coverageAll; coverageReport")
//addCommandAlias("all", "scalafmtAll; compileAll; testAll")
//coverageReport

// sbt scalafixAll && scalafmtWTF && sbt compileAll && sbt testAll && sbt coverageAll && sbt coverageReport
// sbt scalafixAll && scalafmtAll && sbt compileAll && sbt testAll && sbt coverageAll && sbt coverageReport
