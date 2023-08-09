package pl.writeonly.catculus.adt.calculus

import pl.writeonly.catculus.adt.calculus.Lambda._

class LambdaSpec extends org.specs2.mutable.Specification {
  "Generate" >> {
    "a" >> {
      generate(Var("a")) must ===("a")
    }
    "\\a a" >> {
      generate(Abs("a", Var("a"))) must ===("\\a a")
    }
    "`\\a a a" >> {
      generate(App(Abs("a", Var("a")), Var("a"))) must ===("`\\a a a")
    }
    "(\\a a a)" >> {
      generate(apps(Abs("a", Var("a")), Var("a"))) must ===("(\\a a a)")
    }
    "`\\a `a a a" >> {
      generate(App(Abs("a", App(Var("a"), Var("a"))), Var("a"))) must ===("`\\a `a a a")
    }
    "(\\a (a a) a)" >> {
      generate(apps(Abs("a", apps(Var("a"), Var("a"))), Var("a"))) must ===("(\\a (a a) a)")
    }
  }
}