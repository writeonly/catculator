package pl.writeonly.catculus.gadt

//sealed trait Combinator[A]
//
//case class I[A]() extends Combinator[A => A]
//case class K[A, B]() extends Combinator[A => B => A]
//case class S[A, B, C]() extends Combinator[(A => B => C) => (A => B) => A => C]
//
//case class Apply[A, B](func: Combinator[A => B], arg: Combinator[A]) extends Combinator[B]
