package pl.writeonly.catculus.adt.calculus

import cats.data.NonEmptyList
import pl.writeonly.catculus.Extras.fix

object Lambda {

  def generate(l: Lambda): String = l match {
    case Com(c)    => Combinator.generateC(c)
    case Var(n)    => n
    case Abs(n, f) => s"\\$n ${generate(f)}"
    case App(f, x) => s"`${generate(f)} ${generate(x)}"
    case Apps(fs)  => s"(${fs.map(generate).toList.mkString(" ")})"
  }

  def apps(head: Lambda, tail: Lambda*): Lambda = apps1(head, tail.toList)

  def apps1(head: Lambda, tail: List[Lambda]): Lambda = Apps(NonEmptyList(head, tail))

  //I know it is crazy, but I wanted to check it is possible
  private val isOnlyCombinatorStep: (Lambda => Boolean) => Lambda => Boolean = rec => {
    case Com(_)    => true
    case App(f, g) => rec(f) && rec(g)
    case Apps(fs)  => fs.forall(rec)
    case _         => false
  }

  //I know it is crazy, but I wanted to check it is possible
  val isOnlyCombinator: Lambda => Boolean = fix(isOnlyCombinatorStep)

  final case class Com(c: Combinator) extends Lambda
  case class Var(name: String) extends Lambda
  case class Abs(param: String, body: Lambda) extends Lambda
  case class App(f: Lambda, x: Lambda) extends Lambda
  case class Apps(fs: NonEmptyList[Lambda]) extends Lambda
}

sealed trait Lambda
