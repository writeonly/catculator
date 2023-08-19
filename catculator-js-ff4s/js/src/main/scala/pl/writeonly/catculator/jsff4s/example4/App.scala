package pl.writeonly.catculator.jsff4s.example4

import cats.effect.Temporal
import cats.effect.implicits._
import cats.syntax.all._

import scala.concurrent.duration.FiniteDuration

import concurrent.duration._

// A minimal example demonstrating the use of cancellation and running state.

final case class State(counter: Int = 0, loading: Boolean = false)

sealed trait Action

// Increments the counter after waiting for `delay`, unless cancelled.
case class DelayedInc(delay: FiniteDuration) extends Action

// Decrements the counter after waiting for `delay`, unless cancelled.
case class DelayedDec(delay: FiniteDuration) extends Action

case class Inc(amount: Int) extends Action

// Cancels any outstanding inc/dec.
case object Cancel extends Action

case class SetLoadingState(loading: Boolean) extends Action

class App[F[_]](implicit F: Temporal[F]) extends ff4s.App[F, State, Action] {

  private val incCancelKey = "inc"
  private val decCancelKey = "dec"
  private val loadingKey = "loading"

  override val store = ff4s
    .Store[F, State, Action](State()) { store =>
      _ match {
        case Inc(amount) =>
          state => state.copy(counter = state.counter + amount) -> none

        case DelayedInc(delay) =>
          _ -> store
            .withCancellationKey(incCancelKey)(
              store.withRunningState(loadingKey)(
                F.sleep(delay) *> store.dispatch(Inc(1))
              )
            )
            .some

        case DelayedDec(delay) =>
          _ -> store
            .withCancellationKey(decCancelKey)(
              store.withRunningState(loadingKey)(
                F.sleep(delay) *> store.dispatch(Inc(-1))
              )
            )
            .some

        case Cancel =>
          _ -> (
            store.cancel(incCancelKey),
            store.cancel(decCancelKey)
          ).parTupled.void.some

        case SetLoadingState(loading) =>
          _.copy(loading = loading) -> none
      }
    }
    .flatTap { store =>
      store
        .runningState(loadingKey)
        .discrete
        .evalMap(loading => store.dispatch(SetLoadingState(loading)))
        .compile
        .drain
        .background
    }

  import dsl._
  import dsl.html._

  override val view = useState { state =>
    val btnCls = "m-1 p-2 border rounded"
    div(
      span(s"count: ${state.counter}"),
      button(
        cls := btnCls,
        "+",
        onClick := (_ => DelayedInc(1.second).some)
      ),
      button(
        cls := btnCls,
        "-",
        onClick := (_ => DelayedDec(1.second).some)
      ),
      button(
        cls := btnCls,
        "cancel",
        onClick := (_ => Cancel.some)
      ),
      if (state.loading) div("loading...") else empty
    )
  }

}