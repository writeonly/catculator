package pl.writeonly.catculus.udash

import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.document
import org.scalajs.dom.html._

object Main {
  type F2 = (String, String) => String

  val f2: F2 = (s, i) => s + i

  def main(args: Array[String]): Unit = {
    println("Hello UDash")

    loadValueToSource("clean", "")
    loadValueToSource("hello-world", "main = \\ _ -> \"HelloWorld\"")
    loadValueToSource("factorial", "main = \\ _ -> \"factorial\"")
    loadValueToSource("fizzbuzz", "main = \\ _ -> \"fizzbuzz\"")
    loadValueToSource("fibonacci", "main = \\ _ -> \"fibonacci\"")

    calculateValueTo("deSugar")(f2)
    calculateValueTo("compile")(f2)
    calculateValueTo("run")(f2)
  }

  private def loadValueToSource(buttonId: String, value: String): Unit =
    loadValue(buttonId, "source")(() => value)

  private def calculateValueTo(buttonId: String)(f: F2): Unit =
    loadValue(buttonId, "output") { () =>
      f(
        getTextArea("source").value,
        getTextArea("input").value
      )
    }

  private def loadValue(buttonId: String, elementId: String)(f: () => String): Unit =
    getButton(buttonId).onclick = (e: MouseEvent) =>
      document.getElementById(elementId).textContent = f()

  private def getTextArea(elementId: String) = getElementById[TextArea](elementId)
  private def getButton(elementId: String) = getElementById[Button](elementId)

  private def getElementById[A](elementId: String) = document.getElementById(elementId).asInstanceOf[A]
}
