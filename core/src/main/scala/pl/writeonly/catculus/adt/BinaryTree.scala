package pl.writeonly.catculus.adt

import cats.Show
import cats.Show.fromToString

object BinaryTree {
  implicit def binaryTreeShow[A]: Show[BinaryTree[A]] = fromToString
}

sealed trait BinaryTree[+A]

case class Leaf[A](leaf: A) extends BinaryTree[A] //pure
case class Node[A](first: BinaryTree[A], follow: BinaryTree[A]) extends BinaryTree[A] //flatmap
