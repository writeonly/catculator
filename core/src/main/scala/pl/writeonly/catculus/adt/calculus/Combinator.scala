package pl.writeonly.catculus.adt.calculus

import pl.writeonly.catculus.adt.tree._

object Combinator {

  def generateT(tree: Tree[Combinator]): String = tree match {
    case Tree.Leaf(a) => generate(a)
    case Tree.Node(a) => s"(${a.map(generateT).toList.mkString(" ")})"
  }
  def generateBT(tree: BinaryTree[Combinator]): String = tree match {
    case BinaryTree.Leaf(a)    => generate(a)
    case BinaryTree.Node(a, b) => s"`${generateBT(a)} ${generateBT(b)}"
  }

  def generate(c: Combinator): String = c.toString

  case object S extends Combinator

  case object K extends Combinator

  case object I extends Combinator
}

sealed trait Combinator
