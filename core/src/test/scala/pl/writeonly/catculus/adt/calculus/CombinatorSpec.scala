package pl.writeonly.catculus.adt.calculus

import org.specs2.matcher.DataTables
import org.specs2.mutable.Specification
import pl.writeonly.catculus.adt.calculus.Combinator._

class CombinatorSpec extends Specification with DataTables  {
  "Generate Binary" should {
    import pl.writeonly.catculus.adt.tree.BinaryTree._

    "equals" in {
      "ast" | "code" |>
        Leaf(I)                               ! "I"       |
        Node(Leaf(S), Node(Leaf(K), Leaf(K))) ! "`S `K K" |
        { (ast, code) =>
          generateBT(ast) must ===(code)
        }
    }
  }

  "Generate" >> {
    import pl.writeonly.catculus.adt.tree.Tree._

    "equals" in {
      "ast" | "code" |>
        Leaf(I)                               ! "I"         |
        node(Leaf(S), node(Leaf(K), Leaf(K))) ! "(S (K K))" |
        { (ast, code) =>
          generateT(ast) must ===(code)
        }
    }
  }
}