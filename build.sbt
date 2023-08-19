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



Global / onChangedBuildSource := IgnoreSourceChanges // not working well with webpack devserver

name                     := "OutwatchExample"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.11"

val versions = new {
  val outwatch  = "1.0.0-RC15"
  val scalaTest = "3.2.16"
}

// do not warn about unused setting key. TODO: why is this needed? scala-js-bundler bug? sbt says this setting is unused, but it is used.
Global / excludeLintKeys += webpackDevServerPort

val catsVersion = "2.9.0"
val catsEffectVersion = "3.5.1"
val catsMtlVersion = "1.3.1"
val fs2Version = "3.8.0"
//val spec2Version = "4.20.2"
val spec2Version = "4.19.2"
val jawnVersion = "1.5.1"
val scalaTestVersion = "3.2.16"

val coreSettings = Seq(
  name := "catculator",
  moduleName := "catculator-core",

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
    .in(file("catculator-core"))
    .settings(coreSettings)
    .jvmSettings(coreJvmSettings)
    .jsSettings(coreJsSettings)
    .nativeSettings(coreNativeSettings)

// JS

val outwatchVersion = "1.0.0-RC15"
val colibriVersion = "0.7.6"

val outwatchSettings = Seq(
  name := "catculator-outwatch",
  moduleName := "catculator-outwatch",
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "io.github.outwatch"   %%% "outwatch"          % outwatchVersion,
//    "com.github.cornerman" %%% "colibri-fs2"       % colibriVersion,
//    "com.github.cornerman" %%% "colibri-airstream" % colibriVersion,
//    "com.github.cornerman" %%% "colibri-rx"        % colibriVersion,
//    "com.github.cornerman" %%% "colibri-router"    % colibriVersion,
  ),
  Test / javaOptions ++= Seq("-Xmx4g", "-XX:+UseG1GC"),
)

//only for Scala 3 :(
val calicoSettings = Seq(
  name := "catculator-calico",
  moduleName := "catculator-calico",
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "com.armanbilge" %%% "calico" % "0.2.1",
  ),
  Test / javaOptions ++= Seq("-Xmx4g", "-XX:+UseG1GC"),
)

val ff4sSettings = Seq(
  name := "catculator-ff4s",
  moduleName := "catculator-ff4s",
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "io.github.buntec" %%% "ff4s" % "0.17.0",
  ),
  Test / javaOptions ++= Seq("-Xmx4g", "-XX:+UseG1GC"),
)

val scalaJsMacrotaskExecutor = Seq(
  // https://github.com/scala-js/scala-js-macrotask-executor
  libraryDependencies       += "org.scala-js" %%% "scala-js-macrotask-executor" % "1.1.1",
  Compile / npmDependencies += "setimmediate"  -> "1.0.5", // polyfill
)

def readJsDependencies(baseDirectory: File, field: String): Seq[(String, String)] = {
  val packageJson = ujson.read(IO.read(new File(s"$baseDirectory/package.json")))
  packageJson(field).obj.mapValues(_.str).toSeq
}

val outwatch =
  crossProject(JSPlatform)
    .crossType(CrossType.Dummy)
    .enablePlugins(
      ScalaJSPlugin,
      ScalaJSBundlerPlugin,
    )
    .settings(scalaJsMacrotaskExecutor)
    .in(file("catculator-js-outwatch"))
    .dependsOn(core)
    .settings(coreJsSettings)
    .settings(outwatchSettings)
    .settings(
      libraryDependencies ++= Seq(
        "io.github.outwatch" %%% "outwatch" % "1.0.0-RC15",
        "org.scalatest" %%% "scalatest" % "3.2.16" % Test,
      ),
      Compile / npmDependencies ++= readJsDependencies(baseDirectory.value, "dependencies"),
      Compile / npmDevDependencies ++= readJsDependencies(baseDirectory.value, "devDependencies"),
      scalacOptions --= Seq(
        "-Xfatal-warnings",
      ), // overwrite option from https://github.com/DavidGregory084/sbt-tpolecat

      useYarn := true, // Makes scalajs-bundler use yarn instead of npm
      yarnExtraArgs += "--prefer-offline",
      scalaJSLinkerConfig ~= (_.withModuleKind(
        ModuleKind.CommonJSModule,
      )), // configure Scala.js to emit a JavaScript module instead of a top-level script
      scalaJSUseMainModuleInitializer := true, // On Startup, call the main function
      webpackDevServerPort := sys.env
        .get("FRONTEND_PORT")
        .flatMap(port => scala.util.Try(port.toInt).toOption)
        .getOrElse(12345),
      webpackDevServerExtraArgs := Seq("--color"),
      webpack / version := "5.75.0",
      webpackCliVersion := "5.0.0",
      startWebpackDevServer / version := "4.11.1",
      webpackDevServerExtraArgs := Seq("--color"),
      fullOptJS / webpackEmitSourceMaps := true,
      fastOptJS / webpackBundlingMode := BundlingMode.LibraryOnly(),
      fastOptJS / webpackConfigFile := Some(baseDirectory.value / "webpack.config.dev.js"),
      fullOptJS / webpackConfigFile := Some(baseDirectory.value / "webpack.config.prod.js"),
      Test / requireJsDomEnv := true,
    )

val calico =
  crossProject(JSPlatform)
    .crossType(CrossType.Dummy)
    .in(file("catculator-js-calico"))
    .dependsOn(core)
    .jsSettings(coreJsSettings)
    .jsSettings(calicoSettings)

val ff4s =
  crossProject(JSPlatform)
    .crossType(CrossType.Dummy)
    .in(file("catculator-js-ff4s"))
    .dependsOn(core)
    .jsSettings(coreJsSettings)
    .jsSettings(ff4sSettings)

//ROOT
val root = tlCrossRootProject
//  .aggregate(core, outwatch, calico, ff4s)
  .aggregate(core, outwatch, ff4s)
  .settings(
    name := "catculator"
  )

addCommandAlias("prod", "fullOptJS/webpack")
addCommandAlias("dev", "devInit; devWatchAll; devDestroy")
addCommandAlias("devInit", "; outwatchJS/fastOptJS/startWebpackDevServer")
addCommandAlias("devWatchAll", "~; outwatchJS/fastOptJS/webpack")
addCommandAlias("devDestroy", "outwatchJS/fastOptJS/stopWebpackDevServer")

//sbt outwatchJS/fastOptJS
