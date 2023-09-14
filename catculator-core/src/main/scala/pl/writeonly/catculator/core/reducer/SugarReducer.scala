package pl.writeonly.catculator.core.reducer

import cats.data.NonEmptyList
import pl.writeonly.catculator.core.Extras.foldNonEmpty
import pl.writeonly.catculator.core.Extras.ifElse
import pl.writeonly.catculator.core.adt.calculus
import pl.writeonly.catculator.core.adt.calculus.Lambda
import pl.writeonly.catculator.core.adt.calculus.Lambda._
import pl.writeonly.catculator.core.adt.calculus.Sign
import pl.writeonly.catculator.core.adt.calculus.Sign._
import pl.writeonly.catculator.core.adt.calculus._
import spire.math.Natural.End
import spire.math._

object SugarReducer {
  def reduceSugar(l: Lambda): Lambda = l match {
    case c @ Com(_)     => c
    case v @ Var(_)     => v
    case Abs(p, b)      => Abs(p, reduceSugar(b))
    case App(f, g)      => App(reduceSugar(f), reduceSugar(g))
    case MultiApp(fs)   => reduceApps(fs.map(reduceSugar))
    case LocalScope(fs) => reduceApps(fs.map(reduceSugar))
    case NilList(xs)    => reduceNilList(xs.map(reduceSugar))
    case CharStr(s)     => reduceCharStr(s)
    case NatNum(n)      => reduceNatNum(n)
    case IntNum(s, n)   => reduceIntNum(s, n)
  }

  private def reduceApps(l: NonEmptyList[Lambda]): Lambda =
    foldNonEmpty(l)(App.apply)

  private def reduceNilList(xs: List[Lambda]): Lambda = xs
    .foldRight(nilVariable)(wrapAppVireoApp)

  private def reduceCharStr(s: String): Lambda =
    reduceNilList(nilListFromCharStr(s))

  private def nilListFromCharStr(s: String): List[Lambda] = s
    .toList
    .map { c =>
      reduceNatNum(End(UInt(c.toInt)))
    }
    .flatMap { l =>
      List(vireoVariable, l)
    }

  private def reduceNatNum(n: Natural): Lambda =
    ifElse(n.isZero)(zeroVariable)(appSuccVariable(reduceNatNum(n - UInt(1))))

  private def reduceIntNum(s: Sign, n: Natural): Lambda =
    wrapAppVireoApp(reduceSign(s), reduceNatNum(n))

  private def reduceSign(s: Sign): Lambda = s match {
    case Plus  => falseVariable
    case Minus => trueVariable
  }
}