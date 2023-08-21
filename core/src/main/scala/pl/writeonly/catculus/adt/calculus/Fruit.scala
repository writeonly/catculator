package pl.writeonly.catculus.adt.calculus

import pl.writeonly.catculus.adt.calculus.Combinator.generateC
import pl.writeonly.catculus.adt.tree.BinaryTree
import pl.writeonly.catculus.adt.tree.BinaryTree.{Leaf, Node}
import spire.math.Natural

object Fruit {

  def generateFruitBT(tree: BinaryTree[Fruit]): String = tree match {
    case Leaf(a) => generateFruit(a)
    case Node(a, b) => s"`${generateFruitBT(a)} ${generateFruitBT(b)}"
  }

  def generateFruit(f: Fruit): String = f match {
    case Com(c) => generateC(c)
    case Nat(n) => s"$n "
    case Succ   => ":"
  }

  def fromLambda(l: Lambda): BinaryTree[Fruit] = l match {
    case Lambda.Com(c)    => Leaf(Com(c))
    case Lambda.App(f, x) => Node(fromLambda(f), fromLambda(x))
  }

  final case class Com(c: Combinator) extends Fruit
  final case class Nat(n: Natural) extends Fruit
  final case object Succ extends Fruit
}

sealed trait Fruit