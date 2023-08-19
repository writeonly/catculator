package pl.writeonly.catculator.jsff4s.example3

import cats.effect.Async
import cats.effect.implicits._
import cats.syntax.all._
import ff4s.Router
import org.http4s.Uri

case class State(
                  uri: Option[Uri] = None
                )

sealed trait Action

object Action {

  case class SetUri(uri: Uri) extends Action

  case class NavigateTo(uri: Uri) extends Action

}

// This example shows one way to use the built-in router.
class App[F[_]](implicit val F: Async[F]) extends ff4s.App[F, State, Action] {

  private val window = fs2.dom.Window[F]

  override val store = for {

    router <- Router[F](window)

    store <- ff4s.Store[F, State, Action](State())(_ =>
      _ match {
        case Action.NavigateTo(uri) =>
          (_, router.navigateTo(uri).some)
        case Action.SetUri(uri) => _.copy(uri = Some(uri)) -> none
      }
    )

    _ <- router.location.discrete
      .evalMap(uri => store.dispatch(Action.SetUri(uri)))
      .compile
      .drain
      .background

  } yield store

  import dsl._
  import dsl.html._

  val heading = h1(cls := "m-4 text-4xl", "A Router App")

  val currentRoute = useState { state =>
    div(
      cls := "m-1",
      s"Current route: ${state.uri.map(_.renderString).getOrElse("")}"
    )
  }

  override val view = useState { state =>
    val matchedPath = state.uri.flatMap { uri =>
      uri.path.segments match {
        case Vector(path) => Some(path.toString)
        case _            => None
      }
    }

    div(
      cls := "flex flex-col items-center h-screen",
      heading,
      currentRoute,
      div(
        cls := "m-2 flex flex-col items-center",
        p("Navigation"),
        List("foo", "bar", "baz").map { path =>
          div(
            cls := s"m-1 w-full rounded text-center cursor-pointer border ${if (matchedPath == Some(path)) "bg-purple-300"
            else ""}",
            path,
            onClick := (_ =>
              Some(Action.NavigateTo(Uri.unsafeFromString(path)))
              )
          )
        }
      )
    )
  }

}