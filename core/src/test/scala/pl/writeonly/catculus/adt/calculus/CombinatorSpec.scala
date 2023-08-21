package pl.writeonly.catculus.adt.calculus

import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import pl.writeonly.catculus.adt.calculus.Combinator._

class CombinatorSpec extends TableDrivenPropertySpec {
  it should "generate code for BinaryTree of Combinators" in {
    import pl.writeonly.catculus.adt.tree.BinaryTree._
    val combinators = Table(
      ("ast", "code"),
      (Leaf(I), "I"),
      (Node(Leaf(S), Node(Leaf(K), Leaf(K))), "`S `K K"),
    )
    forAll(combinators) { (ast, code) =>
      generateBT(ast) shouldBe code
    }
  }

  it should "generate code for Tree of Combinators" in {
    import pl.writeonly.catculus.adt.tree.Tree._
    val combinators = Table(
      ("ast", "code"),
      (Leaf(I), "I"),
      (node(Leaf(S), node(Leaf(K), Leaf(K))), "(S (K K))"),
    )
    forAll(combinators) { (ast, code) =>
      generateT(ast) shouldBe code
    }
  }
}
