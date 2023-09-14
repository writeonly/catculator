package pl.writeonly.catculator.core.adt.tree

import cats.data.NonEmptyList

object Tree {
  def node[A](head: Tree[A], tail: Tree[A]*): Tree[A] =
    Node(NonEmptyList(head, tail.toList))

  case class Leaf[A](leaf: A) extends Tree[A]

  case class Node[A](children: NonEmptyList[Tree[A]]) extends Tree[A]
}

sealed trait Tree[+A]
