package pl.writeonly.catculator.jsff4s

import cats.effect._
import cats.syntax.all._

object Store {

  // A basic store requires `cats.effect.Concurrent[F]`.
  // In real-world app we usually need the more powerful `cats.effect.Async[F]`.
  def apply[F[_]: Concurrent]: Resource[F, ff4s.Store[F, State, Action]] =
    ff4s.Store[F, State, Action](State()) { _ =>
      _ match {
        case Inc(amount) =>
          state => state.copy(counter = state.counter + amount) -> none
        case Reset => _.copy(counter = 0) -> none
      }
    }

}