
ThisBuild / tlBaseVersion := "1.6"

ThisBuild / developers := List(tlGitHubDev("kamil-adam", "Kamil Adam"))

ThisBuild / crossScalaVersions := Seq(Dependencies.scalaVersion)
ThisBuild / tlVersionIntroduced := Map("3" -> "1.1.5")

ThisBuild / licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))
ThisBuild / startYear := Some(2023)

ThisBuild / tlJdkRelease := Some(8)

ThisBuild / tlSonatypeUseLegacyHost := false
ThisBuild / tlCiReleaseTags := false

lazy val coreSettings = Seq(
  name := "catculator",
  moduleName := "catculator-core",
  libraryDependencies ++= Dependencies.catsDeps.value,
  libraryDependencies ++= Dependencies.catsEffectDeps.value,
  libraryDependencies ++= Dependencies.catsMtlDeps.value,
  libraryDependencies ++= Dependencies.fs2Deps.value,
  libraryDependencies ++= Dependencies.jawnDeps.value,
  libraryDependencies ++= Dependencies.scalaTestDeps.value,
  libraryDependencies ++= Dependencies.specs2Deps.value,
  libraryDependencies ++= Dependencies.disciplineDeps.value,
  libraryDependencies ++= Dependencies.otherDeps.value,
)

val coreJvmSettings = Seq(
  Test / fork := true,
)

val coreJsSettings = Seq(
  scalaJSLinkerConfig ~= {
    _
      .withModuleKind(ModuleKind.ESModule)
  },

)

val coreNativeSettings = Seq(
  tlVersionIntroduced := Map(
    "3" -> "1.5.0",
  ),
)

val core =
  crossProject(NativePlatform, JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("catculator-core"))
    .settings(coreSettings)
    .jvmSettings(coreJvmSettings)
    .jsSettings(coreJsSettings)
    .nativeSettings(coreNativeSettings)

lazy val udash =
  crossProject(JSPlatform)
    .withoutSuffixFor(JSPlatform)
    .crossType(CrossType.Pure)
    .in(file("catculator-udash"))
    .enablePlugins(
      ScalaJSPlugin,
      JSDependenciesPlugin,
    )
    .dependsOn(core)
    .settings(
      mainClass := Some("pl.writeonly.catculus.udash.Main"),
      scalaJSUseMainModuleInitializer := true,

      libraryDependencies ++= Dependencies.uDashDeps.value,
      jsDependencies ++= Dependencies.uDashJSDeps.value,
    )


val rootSettings = Seq(
  name := "catculator",
)

val root =
  tlCrossRootProject
    .aggregate(core)
    .settings(rootSettings)

addCommandAlias("scalafixWTF", "scalafixEnable; scalafixAll")
addCommandAlias("scalafmtWTF", "scalafmtSbt; scalafmtAll")
addCommandAlias("scalaAll", "scalafixWTF; scalafmtAll")

addCommandAlias("compileAll", "clean; compile; Test/compile; test")
addCommandAlias("coreAll", "scalaAll; compileAll; doc")

addCommandAlias("udashAll", "udash/fastOptJS; udash/fullOptJS")
addCommandAlias("coverageAll", "coverage; coreJVM/test; coverageReport")
addCommandAlias("fastAll", "scalaAll; coverageAll")
addCommandAlias("pageAll", "udashAll; coverageAll")

addCommandAlias("all", "coreAll; pageAll")
