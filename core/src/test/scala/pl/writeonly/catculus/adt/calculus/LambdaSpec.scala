package pl.writeonly.catculus.adt.calculus

import pl.writeonly.catculus.adt.calculus.Lambda._
import pl.writeonly.catculus.parsers.LambdaParser._

class LambdaSpec extends org.specs2.mutable.Specification {

  def generateAndParse = parse compose generate

  "Generate" >> {
    "a" >> {
      val a = Var("a")
      generate(a) must ===("a")
      generateAndParse(a) must beRight (a)
    }
    "\\a a" >> {
      val a = Abs("a", Var("a"))
      generate(a) must ===("\\a a")
      generateAndParse(a) must beRight (a)
    }
    "`\\a a a" >> {
      val a = App(Abs("a", Var("a")), Var("a"))
      generate(a) must ===("`\\a a a")
      generateAndParse(a) must beRight (a)
    }
    "(\\a a a)" >> {
      val a = apps(Abs("a", Var("a")), Var("a"))
      generate(a) must ===("(\\a a a)")
      generateAndParse(a) must beRight (a)
    }
    "`\\a `a a a" >> {
      val a = App(Abs("a", App(Var("a"), Var("a"))), Var("a"))
      generate(a) must ===("`\\a `a a a")
      generateAndParse(a) must beRight (a)
    }
    "(\\a (a a))" >> {
      val a = apps(Abs("a", apps(Var("a"), Var("a"))))
      generate(a) must ===("(\\a (a a))")
      generateAndParse(a) must beRight(a)
    }

    "((\\a (a a)) a)" >> {
      val a = apps(apps(Abs("a", apps(Var("a"), Var("a")))), Var("a"))
      generate(a) must ===("((\\a (a a)) a)")
      generateAndParse(a) must beRight(a)
    }

    "(\\a (a a) a)" >> {
      val a = apps(Abs("a", apps(Var("a"), Var("a"))), Var("a"))
      generate(a) must ===("(\\a (a a) a)")
      generateAndParse(a) must beRight(a)
    }
  }
}