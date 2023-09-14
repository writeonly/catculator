package pl.writeonly.catculator.core

import cats.data.NonEmptyList

object Extras {

  def foldNonEmpty[A](l: NonEmptyList[A])(f: (A, A) => A): A = l
    .tail
    .fold(l.head)(f)

  def fix[T, R](f: (T => R) => (T => R)): (T => R) = new Function1[T, R] {
    def apply(t: T): R = f(this)(t)
  }

  // it is any way to have if-else in one line?
  // format: off
  def ifElse[E](c: => Boolean)(e1: => E)(e2: => E): E = if(c) e1 else e2
  // format: on
}
