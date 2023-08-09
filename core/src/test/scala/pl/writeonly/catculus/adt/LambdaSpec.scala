package pl.writeonly.catculus.adt

import cats.derived.MkShow._
import cats.syntax.all._

class LambdaSpec extends org.specs2.mutable.Specification {

  "Lambda" >> {
    "a" >> {
      Leaf(Var("a")).show must ===("Leaf(leaf = Var(name = a))")
    }
    "abs a a" >> {
      Leaf(Abs("a", Var("a"))).show must === ("Leaf(leaf = Abs(param = a, body = Var(name = a)))")
    }
    "(abs a a) a" >> {
      Node(Leaf(Abs("a", Var("a"))), Leaf(Var("a"))).show must === ("Node(first = Leaf(Abs(a,Var(a))), follow = Leaf(Var(a)))")
    }
  }
}