package pl.writeonly.catculator.core.parsers

import cats.parse.Numbers.digits
import cats.parse.strings.Json.delimited.parser as jsonString
import cats.parse.{Parser => P}
import cats.parse.{Parser0 => P0}
import cats.syntax.all.*
import pl.writeonly.catculator.core.adt.calculus
import pl.writeonly.catculator.core.adt.calculus.Lambda.*
import pl.writeonly.catculator.core.adt.calculus.Sign.*
import pl.writeonly.catculator.core.adt.calculus.*
import pl.writeonly.catculator.core.parsers.ParserUtil.*

object LambdaParser {
  private val identifierStart: P[Char] = P.charWhere { c =>
    c.isLetter || "_:;,.".contains(c)
  }
  private val identifierContinue: P[Char] = P.charWhere { c =>
    c.isLetterOrDigit || c === '_'
  }

  val identifier: P[String] =
    symbol((identifierStart ~ identifierContinue.rep0).string)

  // format: off
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
  // format: on

  val variable: P[Lambda] = identifier.map(Var.apply)

  val abstraction: P[Lambda] = (charSymbol('\\') *> identifier ~ lambda)
    .map(Abs.apply)

  val application: P[Lambda] = (charSymbol('`') *> lambda ~ lambda)
    .map(App.apply)

  val mautiApplicationChildren: P[Lambda] = (lambda ~ lambda.rep0)
    .map((multi1 _).tupled)

  val multiApplication: P[Lambda] =
    charSymbol('(') *> mautiApplicationChildren <* charSymbol(')')

  val localScopeChildren: P[Lambda] = (lambda ~ lambda.rep0)
    .map((local1 _).tupled)

  val localScope: P[Lambda] = charSymbol('{') *> localScopeChildren <*
    charSymbol('}')

  val nilList: P[Lambda] = charSymbol('[') *> lambda.rep0.map(NilList.apply) <*
    charSymbol(']')

  val charStr: P[Lambda] = jsonString.map(CharStr.apply)

  val natNum: P[Lambda] = digits.map(natNumFromString)

  val sign: P[Sign] = P
    .charIn("+-")
    .map {
      case '+' => Plus
      case '-' => Minus
    }

  val intNum: P[Lambda] = (sign ~ digits).map((intNumFromString _).tupled)

  val lambdaEnd: P[Lambda] = lambda <* P.end

  val parse: String => Either[P.Error, Lambda] = lambdaEnd.parseAll
}
