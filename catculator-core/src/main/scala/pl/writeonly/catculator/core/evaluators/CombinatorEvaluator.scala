package pl.writeonly.catculator.core.evaluators

import pl.writeonly.catculator.core.adt.calculus.Combinator
import pl.writeonly.catculator.core.adt.calculus.Combinator.*
import pl.writeonly.catculator.core.adt.tree.BinaryTree.*
import pl.writeonly.catculator.core.adt.tree.*

object CombinatorEvaluator {
  def interpret(c: CombinatorBT): CombinatorBT = c match {
    case l@Leaf(_)                 => l
    case Node(Leaf(I), x)          => x
    case Node(Leaf(K), x)          => Leaf(K)
    case Node(Node(Leaf(K), x), y) => x
    case Node(Node(Node(Leaf(S), x), y), z) =>
      val first = Node(x, z)
      val second = Node(y, z)
      Node(first, second)
    case Node(f, x) =>
      val reducedF = interpret(f)
      val reducedX = interpret(x)
      interpret(Node(reducedF, reducedX))
  }
}
