package pl.writeonly.catculator.core.adt.calculus

import cats.data.NonEmptyList
import pl.writeonly.catculator.core.Extras.fix
import spire.math.Natural

object Lambda {

  def generateParams(params: List[String]): String = params
    .map { p =>
      s"\\$p "
    }
    .mkString

  // I know it is crazy, but I wanted to check it is possible
  private val isOnlyCombinatorStep: (Lambda => Boolean) => Lambda => Boolean =
    rec => {
      case Com(_)    => true
      case App(f, g) => rec(f) && rec(g)
      case _         => false
    }

  // I know it is crazy, but I wanted to check it is possible
  val isOnlyCombinator: Lambda => Boolean = fix(isOnlyCombinatorStep)

  def let1(t: (String, Lambda), body: Lambda): Lambda = Let(t._1, t._2, body)

  def multi(params: List[String], body: NonEmptyList[Lambda]): Lambda =
    MultiAbs(params, MultiApp(body))

  def multi1(head: Lambda, tail: List[Lambda]): Lambda =
    MultiApp(NonEmptyList(head, tail))
  def local1(head: Lambda, tail: List[Lambda]): Lambda =
    LocalScope(NonEmptyList(head, tail))

  def natNumFromString(s: String): Lambda = NatNum(Natural(s))

  def natNumFromLong(s: Long): Lambda = NatNum(Natural(s))

  def intNumFromString(sing: Sign, s: String): Lambda = IntNum(sing, Natural(s))

  final case class Com(c: Combinator) extends Lambda
  final case class Var(name: String) extends Lambda
  final case class Abs(param: String, body: Lambda) extends Lambda
  final case class App(f: Lambda, x: Lambda) extends Lambda
  final case class Let(name: String, expression: Lambda, body: Lambda)
      extends Lambda
  final case class MultiAbs(params: List[String], body: Lambda) extends Lambda
  final case class MultiApp(fs: NonEmptyList[Lambda]) extends Lambda
  final case class MultiLet(ps: NonEmptyList[(String, Lambda)], body: Lambda)
      extends Lambda
  final case class LocalScope(xs: NonEmptyList[Lambda]) extends Lambda
  final case class NilList(xs: List[Lambda]) extends Lambda
  final case class CharStr(s: String) extends Lambda
  final case class NatNum(n: Natural) extends Lambda
  final case class IntNum(s: Sign, n: Natural) extends Lambda
}

sealed trait Lambda
