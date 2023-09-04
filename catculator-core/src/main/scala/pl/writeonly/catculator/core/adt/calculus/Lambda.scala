package pl.writeonly.catculator.core.adt.calculus

import cats.data.NonEmptyList
import pl.writeonly.catculator.core.Extras.fix
import spire.math.Natural

object Lambda {

  //  val thrushVariable: Lambda = Var(";")
  val vireoVariable: Lambda = Var(",")
  val nilVariable: Lambda = Var(".")
  val succVariable: Lambda = Var(":")
  val zeroVariable: Lambda = Var("0")
  val falseVariable: Lambda = Var("false")
  val trueVariable: Lambda = Var("true")

  //  def wrapAppThrush(name: String, l1: Lambda, l2: Lambda): Lambda = App(thrushVariable, App(l1, Abs(name, l2)))
  def wrapAppVireoApp(l1: Lambda, l2: Lambda): Lambda = App(App (vireoVariable, l1), l2)
  def appSuccVariable(l: Lambda): Lambda = App(succVariable, l)

  def generate(l: Lambda): String = l match {
    case Com(c)         => Combinator.generateC(c)
    case Var(n)         => n
    case Abs(n, f)      => s"\\$n ${generate(f)}"
    case App(f, x)      => s"`${generate(f)} ${generate(x)}"
    case MultiApp(fs)   => s"(${fs.map(generate).toList.mkString(" ")})"
    case LocalScope(fs) => s"{${fs.map(generate).toList.mkString(" ")}}"
    case NilList(fs)    => s"[${fs.map(generate).mkString(" ")}]"
    case CharStr(s)     => s"\"$s\""
    case NatNum(n)      => n.toString
    case IntNum(s, n)   => Sign.generate(s) + n.toString
  }

//  def apps1(head: Lambda, tail: List[Lambda]): Lambda = Apps(NonEmptyList(head, tail))

  //I know it is crazy, but I wanted to check it is possible
  private val isOnlyCombinatorStep: (Lambda => Boolean) => Lambda => Boolean = rec => {
    case Com(_) => true
    case App(f, g) => rec(f) && rec(g)
    case _ => false
  }

  //I know it is crazy, but I wanted to check it is possible
  val isOnlyCombinator: Lambda => Boolean = fix(isOnlyCombinatorStep)

  def multi1(head: Lambda, tail: List[Lambda]): Lambda = MultiApp(NonEmptyList(head, tail))
  def local1(head: Lambda, tail: List[Lambda]): Lambda = LocalScope(NonEmptyList(head, tail))

  def natNumFromString(s: String): Lambda = NatNum(Natural(s))

  def intNumFromString(sing: Sign, s: String): Lambda = IntNum(sing, Natural(s))

  final case class Com(c: Combinator) extends Lambda
  final case class Var(name: String) extends Lambda
  final case class Abs(param: String, body: Lambda) extends Lambda
  final case class App(f: Lambda, x: Lambda) extends Lambda
  final case class MultiApp(fs: NonEmptyList[Lambda]) extends Lambda
  final case class LocalScope(xs: NonEmptyList[Lambda])extends Lambda
  final case class NilList(xs: List[Lambda]) extends Lambda
  final case class CharStr(s: String) extends Lambda
  final case class NatNum(n: Natural) extends Lambda
  final case class IntNum(s: Sign, n: Natural) extends Lambda
}

sealed trait Lambda
