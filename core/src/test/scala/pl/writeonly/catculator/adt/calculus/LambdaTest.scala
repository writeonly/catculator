package pl.writeonly.catculator.adt.calculus

import pl.writeonly.catculator.UnitSpec
import pl.writeonly.catculator.adt.calculus.Combinator.I
import pl.writeonly.catculator.adt.calculus.Lambda.Com
import pl.writeonly.catculator.reducer.SugarReducer

class LambdaTest extends UnitSpec {
  "A SugarReducer" should {
    "return argument for Combinator" in {
      val i = Com(I)
      SugarReducer.reduceSugar(i) shouldBe i
    }
  }
}
