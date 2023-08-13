package pl.writeonly.catculus.parsers

import cats.parse.{Parser0, Parser => P}
import pl.writeonly.catculus.adt.calculus.Lambda
import pl.writeonly.catculus.adt.calculus.Lambda._

object LambdaParser {
  val identifierStart: P[Char] = P.charWhere(_.isLetter)
  val identifierContinue: P[Char] = P.charWhere(c => c.isLetterOrDigit || c == '_')

  val whitespace: P[Char] = P.charWhere(Character.isWhitespace)
  val whitespaces: Parser0[String] = whitespace.rep0.string

  def symbol[A](a: P[A]): P[A] = a <* whitespaces
  def charSymbol(c: Char): P[Unit] = symbol(P.char(c))

  val identifier: P[String] = symbol((identifierStart ~ identifierContinue.rep0).string)

  val lambda: P[Lambda] = P.defer(
        variable.backtrack
      | abstraction.backtrack
      | application.backtrack
      | parensLambda
  )

  val variable: P[Lambda] = identifier.map(Var)

  val abstraction: P[Lambda] = (charSymbol('\\') *> identifier ~ lambda).map(Abs.tupled)

  val application: P[Lambda] = (charSymbol('`') *> lambda ~ lambda).map(App.tupled)

  val application1: P[Lambda] = (lambda ~ lambda.rep0).map((apps1 _).tupled)

  val parensLambda: P[Lambda] = charSymbol('(') *> application1 <* charSymbol(')')

  val lambdaEnd: P[Lambda] = lambda <* P.end

  val parse: String => Either[P.Error, Lambda] = lambdaEnd.parseAll
}