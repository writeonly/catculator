package pl.writeonly.catculus.reducer

import pl.writeonly.catculus.adt.calculus.Combinator._
import pl.writeonly.catculus.adt.calculus.Lambda
import pl.writeonly.catculus.adt.calculus.Lambda._
import spire.implicits.eqOps

object AbstractionReducer {

  def reduceAbstraction(l: Lambda): Lambda = l match {
    case Abs(p, b) => reduceAbstraction1(p, b)
    case App(f, g) => App(reduceAbstraction(f), reduceAbstraction(g))
    case lambda    => lambda
  }

  private def reduceAbstraction1(p0: String, b0: Lambda): Lambda = b0 match {
    case l if isOnlyCombinator(l) => wrapAppK(l)
    case Var(n1)                  => reduceVar(p0, n1)
    case Abs(p1, b1)              => reduceAbs(p0, p1, b1)
    case App(f, g)                => wrapAppAppS(reduceAbstraction1(p0, f), reduceAbstraction1(p0, g))
    case lambda                   => lambda
  }

  private def reduceVar(n0: String, n1: String): Lambda =
    if (n0 === n1) Com(I) else wrapAppK(Var(n1))

  private def reduceAbs(p0: String, p1: String, b1: Lambda): Lambda =
    reduceAbs1(p0, p1, reduceAbstraction1(p1, b1))

  private def reduceAbs1(p0: String, p1: String, c: Lambda): Lambda =
    if (p0 === p1) wrapAppK(c) else reduceAbstraction1(p0, c)

  private def wrapAppK(c: Lambda): Lambda = App(Com(K), c)

  private def wrapAppAppS(c1: Lambda, c2: Lambda): Lambda = App(App(Com(S), c1), c2)
}
