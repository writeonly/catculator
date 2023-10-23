package pl.writeonly.catculator.core.adt.calculus

import org.scalatest.prop.TableFor3
import pl.writeonly.catculator.core.TableDrivenPropertySpec
import pl.writeonly.catculator.core.adt.calculus.Lambda.*
import pl.writeonly.catculator.core.generators.LambdaGenerator
import pl.writeonly.catculator.core.parsers.LambdaParser
import pl.writeonly.catculator.core.reducer.AbstractionReducer.reduceAbstraction
import pl.writeonly.catculator.core.reducer.SugarReducer.*

class LambdaSpec extends TableDrivenPropertySpec {

  val basicLambda: TableFor3[String, Lambda, String] = Table(
    ("lambda", "ast", "combinators"),
    ("a", Var("a"), "a"),
    ("\\a a", Abs("a", Var("a")), "I"),
    ("`a a", App(Var("a"), Var("a")), "`a a"),
    ("\\a `a a", Abs("a", App(Var("a"), Var("a"))), "``S I I"),
    ("\\a \\a a", Abs("a", Abs("a", Var("a"))), "`K I"),
    (
      "\\a \\b `a b",
      Abs("a", Abs("b", App(Var("a"), Var("b")))),
      "``S ``S `K S ``S `K K I `K I",
    ),
    (
      "\\a \\b `b a",
      Abs("a", Abs("b", App(Var("b"), Var("a")))),
      "``S `K `S I ``S `K K I",
    ),
  )

  val advancedLambda: TableFor3[String, String, String] = Table(
    ("lambda", "desugared", "combinators"),
    ("(a a)", "`a a", "`a a"),
    ("((a a))", "`a a", "`a a"),
    ("(a b c)", "``a b c", "``a b c"),
    ("{a b c}", "``a b c", "``a b c"),
    ("{; (a b) \\c (c d)}", "``; `a b \\c `c d", "``; `a b ``S I `K d"),
    ("(, a .)", "``, a .", "``, a ."),
    ("(, a (, b .))", "``, a ``, b .", "``, a ``, b ."),
    ("[]", ".", "."),
    ("[a]", "``, a .", "``, a ."),
    ("[a b]", "``, a ``, b .", "``, a ``, b ."),
    ("\"\"", ".", "."),
    (
      "\" \"",
      "``, , ``, `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: 0 .",
      "``, , ``, `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: `: 0 .",
    ),
    ("0", "0", "0"),
    ("1", "`: 0", "`: 0"),
    ("+0", "``, false 0", "``, false 0"),
    ("-0", "``, true 0", "``, true 0"),
    ("+1", "``, false `: 0", "``, false `: 0"),
    ("-1", "``, true `: 0", "``, true `: 0"),
  )

  it should "parse basic Lambda and save ATS" in {
    forAll(basicLambda) { (lambda, ast, _) =>
      LambdaParser.parse(lambda).value shouldBe ast
    }
  }

  it should "compile basic Lambda" in {
    forAll(basicLambda) { (lambda, _, combinators) =>
      LambdaParser
        .parse(lambda)
        .map(lambdaSugarReducer.reduceSugar)
        .map(reduceAbstraction)
        .map(LambdaGenerator.generate)
        .value shouldBe combinators
    }
  }

  it should "parse advanced Lambda" in {
    forAll(advancedLambda) { (lambda, _, _) =>
      LambdaParser.parse(lambda).map(LambdaGenerator.generate).value shouldBe
        lambda
    }
  }

  it should "desugar advanced Lambda" in {
    forAll(advancedLambda) { (lambda, desugared, _) =>
      LambdaParser
        .parse(lambda)
        .map(lambdaSugarReducer.reduceSugar)
        .map(LambdaGenerator.generate)
        .value shouldBe desugared
    }
  }

  it should "compile advanced Lambda" in {
    forAll(advancedLambda) { (sugar, _, combinators) =>
      LambdaParser
        .parse(sugar)
        .map(lambdaSugarReducer.reduceSugar)
        .map(reduceAbstraction)
        .map(LambdaGenerator.generate)
        .value shouldBe combinators
    }
  }
}
