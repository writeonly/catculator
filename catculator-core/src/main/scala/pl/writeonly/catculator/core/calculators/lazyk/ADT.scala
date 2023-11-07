package pl.writeonly.catculator.core.calculators.lazyk

import pl.writeonly.catculator.core.adt.calculus.Combinator
import pl.writeonly.catculator.core.adt.calculus.Combinator.CombinatorBT
import pl.writeonly.catculator.core.adt.tree.BinaryTree
import pl.writeonly.catculator.core.adt.tree.BinaryTree.{Leaf, Node}
import spire.math.Natural

object ADT {
  enum ADT:
    case Com(c: Combinator)
    case Num(n: Natural)
    case Succ()

  type ADTBT = BinaryTree[ADT]

  def fromCombinatorBT(c: CombinatorBT): ADTBT = c match {
    case Node(a, b) => Node(fromCombinatorBT(a), fromCombinatorBT(b))
    case Leaf(a) => Leaf(fromCombinator(a))
  }

  def fromCombinator(c: Combinator): ADT = ADT.Com(c)

  def panic(message: String) = throw new IllegalStateException(message)
}
