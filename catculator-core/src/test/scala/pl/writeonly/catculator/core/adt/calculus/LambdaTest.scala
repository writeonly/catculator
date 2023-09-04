package pl.writeonly.catculator.core.adt.calculus

import pl.writeonly.catculator.core.UnitSpec
import pl.writeonly.catculator.core.adt.calculus.Combinator.I
import pl.writeonly.catculator.core.adt.calculus.Lambda.Com
import pl.writeonly.catculator.core.reducer.SugarReducer

class LambdaTest extends UnitSpec {
  "A SugarReducer" should {
    "return argument for Combinator" in {
      val i = Com(I)
      SugarReducer.reduceSugar(i) shouldBe i
    }
  }
}
