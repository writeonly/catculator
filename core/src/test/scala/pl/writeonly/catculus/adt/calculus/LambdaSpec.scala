package pl.writeonly.catculus.adt.calculus

import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.prop.TableFor2
import pl.writeonly.catculus.adt.calculus.Lambda._
import pl.writeonly.catculus.parsers.LambdaParser
import pl.writeonly.catculus.reducer.AbstractionReducer.reduceAbstraction

class LambdaSpec extends TableDrivenPropertySpec {

  val basicLambda: TableFor2[String, Lambda] = Table(
    ("code", "ast"),
    ("a", Var("a")),
    ("\\a a", Abs("a", Var("a"))),
    ("`a a", App(Var("a"), Var("a"))),
    ("\\a `a a", Abs("a", App(Var("a"), Var("a")))),
    ("\\a \\b `a b", Abs("a", Abs("b", App(Var("a"), Var("b"))))),
  )

  val advancedLambda: TableFor2[String, String] =
    Table(
      ("lambda", "combinators"),
      ("\\a a", "I"),
      ("\\a (a a)", "``S I I"),
      ("\\a \\a a", "`K I"),
      ("\\a \\b (a b)", "``S ``S `K S ``S `K K I `K I"),
      ("\\a \\b (b a)", "``S `K `S I ``S `K K I"),
      ("(a a)", "`a a"),
      ("((a a))", "`a a"),
      ("(a b c)", "``a b c"),
    )

  it should "parse basic Lambda and save ATS" in {
    forAll(basicLambda) { (code: String, ast: Lambda) =>
      LambdaParser.parse(code).value shouldBe ast
    }
  }

  it should "parse advanced Lambda" in {
    forAll(advancedLambda) { (sugar: String, _: String) =>
      LambdaParser.parse(sugar).map(generate).value shouldBe sugar
    }
  }

  it should "compile advanced Lambda" in {
    forAll(advancedLambda) { (sugar: String, combinators: String) =>
      LambdaParser.parse(sugar).map(reduceAbstraction).map(generate).value shouldBe combinators
    }
  }
}
