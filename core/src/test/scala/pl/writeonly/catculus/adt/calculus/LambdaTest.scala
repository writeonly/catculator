package pl.writeonly.catculus.adt.calculus

import pl.writeonly.catculus.UnitSpec
import pl.writeonly.catculus.adt.calculus.Combinator.I
import pl.writeonly.catculus.adt.calculus.Lambda.Com
import pl.writeonly.catculus.reducer.SugarReducer

class LambdaTest extends UnitSpec {
  "A SugarReducer" should {
    "return argument for Combinator" in {
      val i = Com(I)
      SugarReducer.reduceSugar(i) shouldBe i
    }
  }
}
