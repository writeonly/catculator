package pl.writeonly.catculator.core.reducer

import pl.writeonly.catculator.core.adt.calculus.Combinator.*
import pl.writeonly.catculator.core.adt.calculus.Lambda
import pl.writeonly.catculator.core.adt.calculus.Lambda.*
import pl.writeonly.catculator.core.adt.tree.BinaryTree.*

object LambdaReducer {
  def toCombinatorBT(l: Lambda): Either[Lambda, CombinatorBT] = l match {
    case Com(c) => Right(Leaf(c))
    case App(f, x) => for {
        fc <- toCombinatorBT(f)
        xc <- toCombinatorBT(x)
      } yield Node(fc, xc)
    case _ => Left(l)
  }
}
