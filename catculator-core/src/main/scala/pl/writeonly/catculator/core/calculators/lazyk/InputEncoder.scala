package pl.writeonly.catculator.core.calculators.lazyk

//import cats.collections.EmptyDequeue.cons
import pl.writeonly.catculator.core.adt.calculus.Combinator
import pl.writeonly.catculator.core.calculators.lazyk.ADT.ADTBT
import pl.writeonly.catculator.core.calculators.lazyk.Constants.*
import spire.math.Natural

object InputEncoder {

//  val end = cons()

  def readInput(input: String): ADTBT = encodeInput(input.toList.map(c => Natural(c.toLong)))

  def encodeInput(input: List[Natural]): ADTBT = input.foldRight(falseCom) { case (n, l) =>
     cons(church(n), l)
  }

  def cons(a: ADTBT, b: ADTBT) = app3(sCom, app3SI(appK(a)), appK(b))

  def church(n: Natural): ADTBT = n match {
    case 0 => falseCom
    case n => succChurch(Natural(n.toBigInt - BigInt(1)))
  }

  def succChurch(n: Natural): ADTBT = successor(church(n))

  def successor(c: ADTBT): ADTBT = app3(sCom, bCom, c)

}
