package pl.writeonly.catculus.adt.calculus

import org.specs2.matcher.DataTables
import org.specs2.mutable.Specification
import pl.writeonly.catculus.adt.calculus.Lambda._
import pl.writeonly.catculus.parsers.LambdaParser._

class LambdaSpec extends Specification with DataTables {

  def generateAndParse = parse compose generate

  "A Lambda" should {
    "equals" in {
      "ast" | "code" |>
        Var("a")                                                 ! "a"               |
        Abs("a", Var("a"))                                       ! "\\a a"           |
        App(Abs("a", Var("a")), Var("a"))                        ! "`\\a a a"        |
        apps(Abs("a", Var("a")), Var("a"))                       ! "(\\a a a)"       |
        App(Abs("a", App(Var("a"), Var("a"))), Var("a"))         ! "`\\a `a a a"     |
        apps(Abs("a", apps(Var("a"), Var("a"))))                 ! "(\\a (a a))"     |
        apps(apps(Abs("a", apps(Var("a"), Var("a")))), Var("a")) ! "((\\a (a a)) a)" |
        apps(Abs("a", apps(Var("a"), Var("a"))), Var("a"))       ! "(\\a (a a) a)"   |
        { (ast, code) =>
          generate(ast) must === (code)
          generateAndParse(ast) must beRight(ast)
        }
    }
  }
}