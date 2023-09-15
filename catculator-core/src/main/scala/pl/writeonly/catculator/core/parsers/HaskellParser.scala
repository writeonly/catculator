package pl.writeonly.catculator.core.parsers

import cats.data.NonEmptyList
import cats.parse.Numbers.digits
import cats.parse.Parser.Expectation
import cats.parse.strings.Json.delimited.parser as jsonString
import cats.parse.Parser as P
import cats.syntax.all.*
import pl.writeonly.catculator.core.adt.calculus.Lambda.*
import pl.writeonly.catculator.core.adt.calculus.*
import pl.writeonly.catculator.core.parsers.ParserUtil.*

object HaskellParser {

  private val identifierStart: P[Char] = P.charWhere { c =>
    c.isLetter
  }
  private val identifierContinue: P[Char] = P.charWhere { c =>
    c.isLetterOrDigit || c === '_'
  }

  private val identifier: P[String] =
    horizontalSymbol((identifierStart ~ identifierContinue.rep0).string)

  // format: off
  private val lambda: P[Lambda] = P.defer(
        variable
      | natNum
      | charStr
      | char
      | abstraction
      | application
  )
  // format: on

  private val variable: P[Lambda] = horizontalSymbol(identifier.map(Var.apply))

  private val application0: P[Lambda] = lambda.rep.map(MultiApp.apply)

  private val application: P[Lambda] = charSymbol('(') *> application0 <*
    charHorizontalSymbol(')')

  private val abstraction0 = charSymbol('\\') *> identifier.rep0 <*
    stringSymbol("->")

  private val abstraction: P[Lambda] = (abstraction0 ~ application0)
    .map(MultiAbs.apply)

  val charStr: P[Lambda] = horizontalSymbol(jsonString.map(CharStr.apply))

  private val apostrophe = P.char('\'')

  val char: P[Lambda] = horizontalSymbol(apostrophe *> P.anyChar <* apostrophe)
    .map { c =>
      natNumFromLong(c.toLong)
    }

  val natNum: P[Lambda] = horizontalSymbol(digits.map(natNumFromString))

  private val function: P[Func] =
    ((identifier <* charSymbol('=')) ~ symbol(lambda)).map(Func.apply)

  private val functionParser: P[Func] = function <* P.end

  private val functionsParser = whitespaces *> function.rep <* P.end

  val parseFunction: String => Either[P.Error, Func] = functionParser.parseAll

  val parseFunctions: String => Either[P.Error, NonEmptyList[Func]] =
    functionsParser.parseAll

  val parse3: String => Either[P.Error, NonEmptyList[Func]] = (s) =>
    parseFunctions(s) match {
      case r @ Right(_) => r
      case Left(v)      => Left(v)
    }

}
