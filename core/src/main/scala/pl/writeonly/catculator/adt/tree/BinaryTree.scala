package pl.writeonly.catculator.adt.tree

object BinaryTree {

  case class Leaf[A](leaf: A) extends BinaryTree[A]

  case class Node[A](first: BinaryTree[A], follow: BinaryTree[A]) extends BinaryTree[A]
}

sealed trait BinaryTree[+A]
