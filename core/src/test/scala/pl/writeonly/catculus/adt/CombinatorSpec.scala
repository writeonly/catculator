package pl.writeonly.catculus.adt

import cats.derived.MkShow._
import cats.syntax.all._

class CombinatorSpec extends org.specs2.mutable.Specification {
  "Combinator" >> {
    "I" >> {
      Leaf(I).show must ===("Leaf(leaf = I$())")
    }
    "SKK" >> {
      Node(Leaf(S), Node(Leaf(K), Leaf(K))).show must === ("Node(first = Leaf(S), follow = Node(Leaf(K),Leaf(K)))")
    }
  }
}