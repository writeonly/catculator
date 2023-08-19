package pl.writeonly.catculus.parsers

import cats.parse.Numbers.digits
import cats.parse.strings.Json.delimited.{parser => jsonString}
import cats.parse.{Parser0, Parser => P}
import pl.writeonly.catculus.adt.calculus.Lambda._
import pl.writeonly.catculus.adt.calculus.Sign._
import pl.writeonly.catculus.adt.calculus._

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

  val lambda: P[Lambda] = P.defer(
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

  val variable: P[Lambda] = identifier.map(Var)

  val abstraction: P[Lambda] = (charSymbol('\\') *> identifier ~ lambda).map(Abs.tupled)

  val application: P[Lambda] = (charSymbol('`') *> lambda ~ lambda).map(App.tupled)

  val mautiApplicationChildren: P[Lambda] = (lambda ~ lambda.rep0).map((multi1 _).tupled)

  val multiApplication: P[Lambda] = charSymbol('(') *> mautiApplicationChildren <* charSymbol(')')

  val localScopeChildren: P[Lambda] = (lambda ~ lambda.rep0).map((local1 _).tupled)

  val localScope: P[Lambda] = charSymbol('{') *> localScopeChildren <* charSymbol('}')

  val nilList: P[Lambda] = charSymbol('[') *> lambda.rep0.map(NilList) <* charSymbol(']')

  val charStr: P[Lambda] = jsonString.map(CharStr)

  val natNum: P[Lambda] = digits.map(natNumFromString)

  val sign: P[Sign] = P.charIn("+-").map {
    case '+' => Plus
    case '-' => Minus
  }

  val intNum: P[Lambda] = (sign ~ digits).map((intNumFromString _).tupled)

  val lambdaEnd: P[Lambda] = lambda <* P.end

  val parse: String => Either[P.Error, Lambda] = lambdaEnd.parseAll
}