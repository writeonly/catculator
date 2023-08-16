package pl.writeonly.catculus.adt.calculus

object Lazy {
  final case class Com(c: Combinator) extends Lazy
  final case class Nat(n: Natural) extends Lazy
  final case object Succ extends Lazy
}

sealed trait Lazy
