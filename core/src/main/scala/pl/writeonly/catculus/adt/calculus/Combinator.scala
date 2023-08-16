package pl.writeonly.catculus.adt.calculus

import pl.writeonly.catculus.adt.tree._

object Combinator {

  def generateT(tree: Tree[Combinator]): String = tree match {
    case Tree.Leaf(a) => generateC(a)
    case Tree.Node(a) => s"(${a.map(generateT).toList.mkString(" ")})"
  }
  def generateBT(tree: BinaryTree[Combinator]): String = tree match {
    case BinaryTree.Leaf(a)    => generateC(a)
    case BinaryTree.Node(a, b) => s"`${generateBT(a)} ${generateBT(b)}"
  }

  def generateC(c: Combinator): String = c.toString

  final case object S extends Combinator
  final case object K extends Combinator
  final case object I extends Combinator
}

sealed trait Combinator
