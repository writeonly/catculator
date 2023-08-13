package pl.writeonly.catculus.adt.calculus

import cats.data.NonEmptyList

object Lambda {

  def generate(l: Lambda): String = l match {
    case Var(n)    => n
    case Abs(n, f) => s"\\$n ${generate(f)}"
    case App(f, x) => s"`${generate(f)} ${generate(x)}"
    case Apps(fs)  => s"(${fs.map(generate).toList.mkString(" ")})"
  }

  def apps(head: Lambda, tail: Lambda*): Lambda = apps1(head, tail.toList)

  def apps1(head: Lambda, tail: List[Lambda]): Lambda = Apps(NonEmptyList(head, tail))
  case class Var(name: String) extends Lambda

  case class Abs(param: String, body: Lambda) extends Lambda

  case class App(f: Lambda, x: Lambda) extends Lambda

  case class Apps(fs: NonEmptyList[Lambda]) extends Lambda
}

sealed trait Lambda
