package pl.writeonly.catculator.core.parsers

import cats.implicits.catsSyntaxEq
import cats.parse.Parser.Expectation
import cats.parse.{Parser as P, Parser0 as P0, *}

object ParserUtil {
  val whitespace: P[Char] = P.charWhere(Character.isWhitespace)
  val whitespaces: P0[String] = whitespace.rep0.string

  val horizontalWhitespace: P[Char] = P.charWhere { c =>
    Character.isWhitespace(c) && (c =!= '\n')
  }

  val horizontalWhitespaces: P0[String] = horizontalWhitespace.rep0.string

  def literal(s: String): P[Unit] = P.string(s).void

  def symbol[A](a: P[A]): P[A] = a <* whitespaces

  def horizontalSymbol[A](a: P[A]): P[A] = a <* horizontalWhitespaces

  def charSymbol(c: Char): P[Unit] = symbol(P.char(c))

  def charHorizontalSymbol(c: Char): P[Unit] = horizontalSymbol(P.char(c))

  def stringSymbol(s: String): P[Unit] = symbol(P.string(s))

  def stringHorizontalSymbol(s: String): P[Unit] = horizontalSymbol(P.string(s))

  def aaa(e: Expectation)(input: String): Expectation = e match {
    case Expectation.EndOfString(offset, length) => e
    case _                                       => e
  }

  def split(input: String) = input
    .split("\n")
    .map { s =>
      (s, s.length)
    }
    .foldLeft((0, List[(String, Int)]())) {
      case ((c, l: List[(String, Int)]), e) =>
        val newC = c + e._2
        val t = (e._1, newC)
        val newL: List[(String, Int)] = l :+ t
        (newC, newL)

    }
}
