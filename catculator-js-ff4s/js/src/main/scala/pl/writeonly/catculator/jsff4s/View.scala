package pl.writeonly.catculator.jsff4s

object View {

  def apply[F[_]](implicit dsl: ff4s.Dsl[F, State, Action]) = {

    import dsl._
    import dsl.html._

    useState { state =>
      div(
        cls := "counter-example", // cls b/c class is a reserved keyword in scala
        div(s"value: ${state.counter}"),
        button(
          cls := "counter-button",
          "Increment",
          onClick := (_ => Some(Inc(1)))
        ),
        button(
          cls := "counter-button",
          "Decrement",
          onClick := (_ => Some(Inc(-1)))
        ),
        button(
          cls := "counter-button",
          "Reset",
          onClick := (_ => Some(Reset))
        )
      )
    }

  }
}