package pl.writeonly.catculator.core.calculators

import pl.writeonly.catculator.core.adt.calculus.Combinator.CombinatorBT
import pl.writeonly.catculator.core.adt.tree.BinaryTree.Leaf
import pl.writeonly.catculator.core.adt.tree.BinaryTree.Node

object CombinatorCalculator {

  def calculate(c: CombinatorBT) = c match {
    case Node(f, x) =>
    case Leaf(a)    =>
  }

}
