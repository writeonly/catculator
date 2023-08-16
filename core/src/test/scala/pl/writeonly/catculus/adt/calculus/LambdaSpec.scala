package pl.writeonly.catculus.adt.calculus

import cats.parse.Parser
import org.specs2.matcher.DataTables
import org.specs2.mutable.Specification
import pl.writeonly.catculus.reducer.AbstractionReducer._
import pl.writeonly.catculus.adt.calculus.Combinator._
import pl.writeonly.catculus.adt.calculus.Lambda._
import pl.writeonly.catculus.parsers.LambdaParser._

class LambdaSpec extends Specification with DataTables {

  val generateAndParse: Lambda => Either[Parser.Error, Lambda] = parse compose generate

  def parseAndReduceAbstraction(code : String) =
    parse(code).map(reduceAbstraction)

  "A Lambda" should {
    "parse" in {
      "ast" | "code" |>
        Var("a")                                                 ! "a"               |
        Abs("a", Var("a"))                                       ! "\\a a"           |
        Abs("a", App(Var("a"), Var("a")))                        ! "\\a `a a"        |
        Abs("a", apps(Var("a"), Var("a")))                       ! "\\a (a a)"       |
        App(Abs("a", Var("a")), Var("a"))                        ! "`\\a a a"        |
        apps(Abs("a", Var("a")), Var("a"))                       ! "(\\a a a)"       |
        App(Abs("a", App(Var("a"), Var("a"))), Var("a"))         ! "`\\a `a a a"     |
        apps(Abs("a", apps(Var("a"), Var("a"))))                 ! "(\\a (a a))"     |
        apps(apps(Abs("a", apps(Var("a"), Var("a")))), Var("a")) ! "((\\a (a a)) a)" |
        apps(Abs("a", apps(Var("a"), Var("a"))), Var("a"))       ! "(\\a (a a) a)"   |
        { (ast, code) =>
          parse(code) must beRight(ast)
          generate(ast) must === (code)
        }
    }

    "reduce abstraction" in {
      "combinator" | "lambda code" |>
        Com(I)                                                                                                             ! "\\a a"         |
        App(App(Com(S),Com(I)),Com(I))                                                                                     ! "\\a `a a"      |
        App(App(Com(S),Com(I)),Com(I))                                                                                     ! "\\a (a a)"     |
        App(App(Com(S),App(Com(K),Com(K))),Com(I))                                                                         ! "\\a \\b a"     |
        App(Com(K),Com(I))                                                                                                 ! "\\a \\b b"     |
        App(App(Com(S),App(App(Com(S),App(Com(K),Com(S))),App(App(Com(S),App(Com(K),Com(K))),Com(I)))),App(Com(K),Com(I))) ! "\\a \\b `a b"  |
        App(App(Com(S),App(App(Com(S),App(Com(K),Com(S))),App(App(Com(S),App(Com(K),Com(K))),Com(I)))),App(Com(K),Com(I))) ! "\\a \\b (a b)" |
        App(App(Com(S),App(Com(K),App(Com(S),Com(I)))),App(App(Com(S),App(Com(K),Com(K))),Com(I)))                         ! "\\a \\b `b a"  |
        App(App(Com(S),App(Com(K),App(Com(S),Com(I)))),App(App(Com(S),App(Com(K),Com(K))),Com(I)))                         ! "\\a \\b (b a)" |
        { (combinator, code) =>
          parseAndReduceAbstraction(code) must beRight(combinator)
        }
    }

  }
}