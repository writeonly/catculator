package pl.writeonly.catculator.core.reducer

import cats.data.NonEmptyList
import pl.writeonly.catculator.core.Extras.foldNonEmpty
import pl.writeonly.catculator.core.Extras.ifElse
import pl.writeonly.catculator.core.LambdaConfig
import pl.writeonly.catculator.core.adt.calculus
import pl.writeonly.catculator.core.adt.calculus.Lambda.*
import pl.writeonly.catculator.core.adt.calculus.Sign.*
import pl.writeonly.catculator.core.adt.calculus.*
import spire.math.Natural.End
import spire.math.*

object SugarReducer {
  val haskellSugarReducer = new SugarReducer(LambdaConfig.haskellConfig)
  val lambdaSugarReducer = new SugarReducer(LambdaConfig.lambdaConfig)
}

class SugarReducer(config: LambdaConfig) {

  def reduceSugar(l: Lambda): Lambda = l match {
    case c @ Com(_) => c
    case v @ Var(_) => v
    case Abs(p, b)  => Abs(p, reduceSugar(b))
    case App(f, g)  => App(reduceSugar(f), reduceSugar(g))

    case Let(n, e, b)    => config.wrapAppThrush(e, n)(b)
    case MultiAbs(ps, b) => reduceAbss(ps, reduceSugar(b))
    case MultiApp(fs)    => reduceApps(fs.map(reduceSugar))
    case MultiLet(ps, b) => reduceSugar(reduceLets(ps, b))
    case LocalScope(fs)  => reduceApps(fs.map(reduceSugar))
    case NilList(xs)     => reduceNilList(xs.map(reduceSugar))
    case CharStr(s)      => reduceCharStr(s)
    case NatNum(n)       => reduceNatNum(n)
    case IntNum(s, n)    => reduceIntNum(s, n)
  }

  private def reduceAbss(params: List[String], body: Lambda): Lambda = params
    .foldRight(body)(Abs.apply)

  private def reduceApps(l: NonEmptyList[Lambda]): Lambda =
    foldNonEmpty(l)(App.apply)

  def reduceLets(ps: NonEmptyList[(String, Lambda)], body: Lambda) = MultiApp(
    NonEmptyList(MultiAbs(ps.map(_._1).toList, body), ps.map(_._2).toList),
  )

  private def reduceNilList(xs: List[Lambda]): Lambda = xs
    .foldRight(config.nilVariable)(config.wrapAppVireoApp)

  private def reduceCharStr(s: String): Lambda =
    reduceNilList(nilListFromCharStr(s))

  private def nilListFromCharStr(s: String): List[Lambda] = s
    .toList
    .map { c =>
      reduceNatNum(End(UInt(c.toInt)))
    }
    .flatMap { l =>
      List(config.vireoVariable, l)
    }

  private def reduceNatNum(n: Natural): Lambda = ifElse(n.isZero)(
    config.zeroVariable,
  )(config.appSuccVariable(reduceNatNum(n - UInt(1))))

  private def reduceIntNum(s: Sign, n: Natural): Lambda = config
    .wrapAppVireoApp(reduceSign(s), reduceNatNum(n))

  private def reduceSign(s: Sign): Lambda = s match {
    case Plus  => config.falseVariable
    case Minus => config.trueVariable
  }
}
