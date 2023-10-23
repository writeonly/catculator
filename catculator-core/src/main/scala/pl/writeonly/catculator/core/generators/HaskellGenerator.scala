package pl.writeonly.catculator.core.generators

import pl.writeonly.catculator.core.adt.calculus.Combinator
import pl.writeonly.catculator.core.adt.calculus.Combinator.*
import pl.writeonly.catculator.core.adt.calculus.Lambda
import pl.writeonly.catculator.core.adt.calculus.Lambda.*
import pl.writeonly.catculator.core.adt.calculus.Sign

object HaskellGenerator {
  def generate(l: Lambda): String = l match {
    case Com(c)          => Combinator.generateC(c)
    case Var(n)          => n
    case Abs(n, f)       => s"\\$n ${generate(f)}"
    case App(f, x)       => s"`${generate(f)} ${generate(x)}"
    case Let(n, e, b)    => s"let $n = ${generate(e)} in ${generate(b)}"
    case MultiAbs(ns, b) => s"${generateParams(ns)}${generate(b)}"
    case MultiApp(fs)    => s"(${fs.map(generate).toList.mkString(" ")})"
    case MultiLet(es, b) => s"let $es in $b"
    case LocalScope(fs)  => s"{${fs.map(generate).toList.mkString(" ")}}"
    case NilList(fs)     => s"[${fs.map(generate).mkString(" ")}]"
    case CharStr(s)      => s"\"$s\""
    case NatNum(n)       => n.toString
    case IntNum(s, n)    => Sign.generate(s) + n.toString
  }
}
