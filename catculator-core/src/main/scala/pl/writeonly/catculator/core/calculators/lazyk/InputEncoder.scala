package pl.writeonly.catculator.core.calculators.lazyk

import pl.writeonly.catculator.core.calculators.lazyk.ADT.ADTBT
import pl.writeonly.catculator.core.calculators.lazyk.Constants.*
import spire.math.Natural

object InputEncoder {

  def readInput(input: String): ADTBT = encodeInput(input.toList.map { c =>
    Natural(c.toLong)
  })

  private def encodeInput(input: List[Natural]): ADTBT = input.foldRight(falseCom) { case (n, l) =>
     cons(church(n), l)
  }

  def cons(a: ADTBT, b: ADTBT): ADTBT = app3(sCom, app3SI(appK(a)), appK(b))

  private def church(n: Natural): ADTBT = n.toBigInt match {
    case n if n == BigInt(0)  => falseCom
    case n => succChurch(Natural(n - BigInt(1)))
  }

  private def succChurch(n: Natural): ADTBT = successor(church(n))

  private def successor(c: ADTBT): ADTBT = app3(sCom, bCom, c)

}
