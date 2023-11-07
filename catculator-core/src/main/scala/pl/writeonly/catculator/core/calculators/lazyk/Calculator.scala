package pl.writeonly.catculator.core.calculators.lazyk

import pl.writeonly.catculator.core.adt.calculus.Combinator
import pl.writeonly.catculator.core.adt.calculus.Combinator.*
import pl.writeonly.catculator.core.adt.tree.BinaryTree
import pl.writeonly.catculator.core.adt.tree.BinaryTree.*
import pl.writeonly.catculator.core.calculators.lazyk.ADT.*
import pl.writeonly.catculator.core.calculators.lazyk.Reducer.flippedApply
import spire.math.Natural

object Calculator {

  val number0: ADTBT = Leaf(ADT.Num(Natural(0)))

  val trueVar: ADTBT = Leaf(ADT.Com(K))

  val falseVar: ADTBT = Node(trueVar, Leaf(ADT.Com(I)))

  def run(program: ADTBT): Unit = runWithTerminator(falseVar, program)

  def runWithTerminator(termanator: ADTBT, combinator: ADTBT): Unit =
    output(termanator, combinator, realizeWithTrue(combinator))

  def realizeWithTrue(combinator: ADTBT): Natural = realize(flippedApply(trueVar, combinator))

  def realize(combinator: ADTBT): Natural = naturalSafe(flippedApply(number0, flippedApply(Leaf(ADT.Succ()), combinator)))

  def naturalSafe(combinator: ADTBT): Natural = combinator match {
    case Leaf(ADT.Num(x)) => x
    case x => panic(s"Invalid output format. Output should be the list of Church numerals. $x")
  }

  def output(termanator: ADTBT, combinator: ADTBT, number: Natural): Unit = {
    println(number)
    runWithTerminator(termanator, Reducer.apply(combinator, termanator))
  }

}
