addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.4.14")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.13.2")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.3.2")
addSbtPlugin("org.typelevel" % "sbt-typelevel" % "0.4.22")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.0")
addSbtPlugin("org.typelevel" % "sbt-tpolecat" % "0.5.0")

//addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "1.2.2")
//addSbtPlugin("scalacenter" % "sbt-scalajs-bundler" % "0.21.1")
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.21.1")

libraryDependencies ++= Seq("com.lihaoyi" %% "upickle" % "3.1.2")
