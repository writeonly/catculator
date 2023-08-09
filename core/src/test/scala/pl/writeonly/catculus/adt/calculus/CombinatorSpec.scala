package pl.writeonly.catculus.adt.calculus

import pl.writeonly.catculus.adt.calculus.Combinator._

class CombinatorSpec extends org.specs2.mutable.Specification {
  "Generate Binary" >> {
    import pl.writeonly.catculus.adt.tree.BinaryTree._
    "I" >> {
      generateBT(Leaf(I)) must ===("I")
    }
    "SKK" >> {
      generateBT(Node(Leaf(Combinator.S), Node(Leaf(K), Leaf(K)))) must ===("`S `K K")
    }
  }
  "Generate" >> {
    import pl.writeonly.catculus.adt.tree.Tree._
    "I" >> {
      generateT(Leaf(I)) must ===("I")
    }
    "SKK" >> {
      generateT(node(Leaf(S), node(Leaf(K), Leaf(K)))) must ===("(S (K K))")
    }
  }
}