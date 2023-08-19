package pl.writeonly.catculator.jsoutwatch

import outwatch._
import outwatch.dsl._
import colibri._
import cats.effect._
object Main extends IOApp.Simple {
  override def run: cats.effect.IO[Unit] = {
    val code = Subject.behavior("")
    val input = Subject.behavior("")
    val output = Subject.behavior("")


    val component = div(
      textArea(
        onInput.value --> code,
      ),
      textArea(
        onInput.value --> input,
      ),
      button("Run1", onClick(for {
        c <- code
        i <- input
      } yield f(c, i)) --> output),
      button("Run2", onClick(for {
        c <- code
        i <- input
      } yield f(i, c)) --> output),
      div("output: ", output),
      div("code: ", code),
      div("input: ", input),
    )

    Outwatch.renderInto[IO]("#app", component)
  }

//  def apply(): VNode = {
//    val textArea1Handler = createStringHandler()
//    val textArea2Handler = createStringHandler()
//    val buttonClickHandler = createHandler[MouseEvent]()
//
//    val combinedText = buttonClickHandler
//      .withLatestFrom(textArea1Handler, textArea2Handler)((_, text1, text2) => s"$text1 $text2")
//
//    div(
//      textarea(inputString(textArea1Handler)),
//      textarea(inputString(textArea2Handler)),
//      button("Połącz Tekst", onClick(buttonClickHandler)),
//      div(child <-- combinedText)
//    )
//  }

  def f(s1: String, s2: String): String = s1 + s2

}
