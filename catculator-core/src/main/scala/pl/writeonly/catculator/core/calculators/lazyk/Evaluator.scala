package pl.writeonly.catculator.core.calculators.lazyk

import pl.writeonly.catculator.core.adt.calculus.Combinator.CombinatorBT
import pl.writeonly.catculator.core.adt.tree.BinaryTree.Node
import pl.writeonly.catculator.core.calculators.lazyk.ADT.ADTBT
import pl.writeonly.catculator.core.calculators.lazyk.Calculator.run
import pl.writeonly.catculator.core.calculators.lazyk.InputEncoder.readInput
import pl.writeonly.catculator.core.calculators.lazyk.Reducer.reduce

object Evaluator {
  


  def evalCombinator(combinator: ADTBT, input: String): Unit =
    run(reduce(Node(combinator, readInput(input))))

}
