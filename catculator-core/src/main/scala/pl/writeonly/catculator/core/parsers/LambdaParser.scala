package pl.writeonly.catculator.core.parsers

import cats.parse.Numbers.digits
import cats.parse.strings.Json.delimited.{parser => jsonString}
import cats.parse.{Parser0, Parser => P}
import pl.writeonly.catculator.core.adt.calculus
import pl.writeonly.catculator.core.adt.calculus.Lambda._
import pl.writeonly.catculator.core.adt.calculus.Sign
import pl.writeonly.catculator.core.adt.calculus.Sign._

object LambdaParser {
  val identifierStart: P[Char] = P.charWhere(c => c.isLetter || "_:;,.".contains(c))
//  val identifierStart: P[Char] = P.charWhere(_.isLetter)
  val identifierContinue: P[Char] = P.charWhere(c => c.isLetterOrDigit || c == '_')

  val whitespace: P[Char] = P.charWhere(Character.isWhitespace)
  val whitespaces: Parser0[String] = whitespace.rep0.string

  def symbol[A](a: P[A]): P[A] = a <* whitespaces
  def charSymbol(c: Char): P[Unit] = symbol(P.char(c))

//  val identifier: P[String] = symbol((identifierChar ~ identifierChar.rep0).string)
  val identifier: P[String] = symbol((identifierStart ~ identifierContinue.rep0).string)

  val lambda: P[calculus.Lambda] = P.defer(
        variable
      | abstraction
      | application
      | multiApplication
      | localScope
      | nilList
      | charStr
      | natNum
      | intNum
  )

  val variable: P[calculus.Lambda] = identifier.map(Var)

  val abstraction: P[calculus.Lambda] = (charSymbol('\\') *> identifier ~ lambda).map(Abs.tupled)

  val application: P[calculus.Lambda] = (charSymbol('`') *> lambda ~ lambda).map(App.tupled)

  val mautiApplicationChildren: P[calculus.Lambda] = (lambda ~ lambda.rep0).map((multi1 _).tupled)

  val multiApplication: P[calculus.Lambda] = charSymbol('(') *> mautiApplicationChildren <* charSymbol(')')

  val localScopeChildren: P[calculus.Lambda] = (lambda ~ lambda.rep0).map((local1 _).tupled)

  val localScope: P[calculus.Lambda] = charSymbol('{') *> localScopeChildren <* charSymbol('}')

  val nilList: P[calculus.Lambda] = charSymbol('[') *> lambda.rep0.map(NilList) <* charSymbol(']')

  val charStr: P[calculus.Lambda] = jsonString.map(CharStr)

  val natNum: P[calculus.Lambda] = digits.map(natNumFromString)

  val sign: P[Sign] = P.charIn("+-").map {
    case '+' => Plus
    case '-' => Minus
  }

  val intNum: P[calculus.Lambda] = (sign ~ digits).map((intNumFromString _).tupled)

  val lambdaEnd: P[calculus.Lambda] = lambda <* P.end

  val parse: String => Either[P.Error, calculus.Lambda] = lambdaEnd.parseAll
}