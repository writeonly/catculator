package pl.writeonly.catculator.jsff4s

import cats.effect._
import cats.syntax.all._

// App.scala
class App[F[_]](implicit F: Concurrent[F]) extends ff4s.App[F, State, Action] {
  override val store = Store[F]
  override val view = View[F]
}