package pl.writeonly.catculator.core.adt.calculus

import pl.writeonly.catculator.core.adt.tree.*

object Combinator {

  type CombinatorT = Tree[Combinator]
  type CombinatorBT = BinaryTree[Combinator]

  def generateT(tree: CombinatorT): String = tree match {
    case Tree.Leaf(a) => generateC(a)
    case Tree.Node(a) => s"(${a.map(generateT).toList.mkString(" ")})"
  }
  def generateBT(tree: CombinatorBT): String = tree match {
    case BinaryTree.Leaf(a)    => generateC(a)
    case BinaryTree.Node(a, b) => s"`${generateBT(a)} ${generateBT(b)}"
  }

  def generateC(c: Combinator): String = c.toString

  case object S extends Combinator
  case object K extends Combinator
  case object I extends Combinator
}

sealed trait Combinator
