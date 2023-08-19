package pl.writeonly.catculator.parsers

import cats.parse.{Parser => P, Parser0 => P0}
import pl.writeonly.catculator.adt.calculus.Lambda
import pl.writeonly.catculator.adt.calculus.Lambda._

object HaskellParser {

  val whitespace: P0[Unit] = P.charWhere(_.isWhitespace).rep0.void

  def symbol[A](a: P[A]): P[A] = a <* whitespace

  val identifier: P[String] = (P.charWhere(_.isLetter) ~ P.charWhere(c => c.isLetterOrDigit || c == '_').rep0).string

  val lambda: P[Lambda] = P.defer(
    lambdaVar | lambdaAbs | lambdaApp
  )

  val lambdaVar: P[Lambda] = identifier.map(Var)

  val lambdaAbs: P[Lambda] = (P.string("\\") *> identifier ~ (P.string("->") *> lambda)).map(Abs.tupled)

  val lambdaApp: P[Lambda] = (symbol(lambda) ~ lambda).map(App.tupled)

//  val lambda: P[Lambda] = P.recursive {
//    P.oneOf(lambdaVar :: lambdaAbs :: lambdaApp :: Nil)
//  }



  val lambdaEnd: P[Lambda] = lambda <* P.end

  val parse: String => Either[P.Error, Lambda] = lambdaEnd.parseAll

}