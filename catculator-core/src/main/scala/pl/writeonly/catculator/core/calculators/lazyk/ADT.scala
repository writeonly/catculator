package pl.writeonly.catculator.core.calculators.lazyk

import pl.writeonly.catculator.core.adt.calculus.Combinator
import pl.writeonly.catculator.core.adt.tree.BinaryTree
import spire.math.Natural

object ADT {
  enum ADT:
    case Com(c: Combinator)
    case Num(n: Natural)
    case Succ()

  type ADTBT = BinaryTree[ADT]

  def panic(message: String) = throw new IllegalStateException(message)
}
