package pl.writeonly.catculator.core.calculators.lazyk

import pl.writeonly.catculator.core.adt.calculus.Combinator.*
import pl.writeonly.catculator.core.adt.tree.BinaryTree.*
import pl.writeonly.catculator.core.calculators.lazyk.ADT.*
import spire.math.Natural

object Reducer {
  def reduce(f: ADTBT): ADTBT = f match {
    case Node(x, y) => apply(reduce(x), reduce(y))
    case x => x
  }

  def flippedApply(x: ADTBT, y : ADTBT): ADTBT = apply(y, x)

  def apply(f: ADTBT, x: ADTBT): ADTBT = (f, x) match {
    case (Node(Node(Leaf(ADT.Com(S)), x), y), z) => Node(Node(x, z), Node(y, z))
    case (Node(Leaf(ADT.Com(K)), x), _) => x
    case (Leaf(ADT.Com(I)), x) => x
    case (Leaf(ADT.Succ()), Leaf(ADT.Num(x))) => Leaf(ADT.Num(x + Natural(1)))
    case (Leaf(ADT.Succ()), _) => panic(s"attempted to apply inc to a non-number $x")
    case _ => Node(f, x)
  }
}
