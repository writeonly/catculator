ThisBuild / tlBaseVersion := "1.6"

ThisBuild / developers := List(
  tlGitHubDev("kamil-adam", "Kamil Adam")
)

//ThisBuild / crossScalaVersions := Seq("2.13.11", "3.2.1")
ThisBuild / crossScalaVersions := Seq("2.13.11")
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
    "org.typelevel" %%% "cats-effect-laws" % catsEffectVersion % Test
  ),
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-mtl" % catsMtlVersion,
    "org.typelevel" %%% "cats-mtl-laws" % catsMtlVersion % Test
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
    "org.specs2" %%% "specs2-scalacheck" % spec2Version % Test
  ),
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "spire" % "0.18.0",

    "org.typelevel" %%% "cats-parse" % "0.3.10",
    "org.typelevel" %%% "log4cats-core" % "2.6.0",

    "org.scalacheck" %%% "scalacheck" % "1.17.0" % Test,

    //    "org.scalameta" %%% "munit" % "1.0.0" % Test,
    //    "org.scalameta" %%% "munit-scalacheck" % "0.7.29" % Test,
  ),

  Test / javaOptions ++= Seq("-Xmx4g", "-XX:+UseG1GC"),
)

val coreJvmSettings = Seq(
  Test / fork := true,
  Test / javaOptions ++= Seq("-Xmx2g", "-XX:+UseG1GC"),
)

val coreJsSettings = Seq(
  tlVersionIntroduced ~= {
    _ ++ List("2.13").map(_ -> "1.0.2").toMap
  }
)

val coreNativeSettings = Seq(
  tlVersionIntroduced := Map(
    "2.13" -> "1.1.3",
    "3" -> "1.5.0"
  )
)

val core =
  crossProject(NativePlatform, JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("core"))
    .settings(coreSettings)
    .jvmSettings(coreJvmSettings)
    .jsSettings(coreJsSettings)
    .nativeSettings(coreNativeSettings)

val root =
  tlCrossRootProject
    .aggregate(core)
    .settings(
      name := "catculus"
    )