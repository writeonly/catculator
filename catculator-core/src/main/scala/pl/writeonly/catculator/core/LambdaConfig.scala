package pl.writeonly.catculator.core

import cats.data.NonEmptyList
import mouse.all.anySyntaxMouse
import pl.writeonly.catculator.core.adt.calculus.Lambda
import pl.writeonly.catculator.core.adt.calculus.Lambda.*

object LambdaConfig {
  val haskellConfig: LambdaConfig = LambdaConfig(
    apply = "apply",
    pair = "pair",
    nil = "nil",
    succ = "succ",
    zero = "zero",
  )
  val lambdaConfig: LambdaConfig =
    LambdaConfig(apply = ";", pair = ",", nil = ".", succ = ":", zero = "0")
}

case class LambdaConfig(
  apply: String,
  pair: String,
  nil: String,
  succ: String,
  zero: String,
) {

  val thrushVariable: Var = Var(apply)
  val vireoVariable: Lambda = Var(pair)
  val nilVariable: Lambda = Var(nil)
  val succVariable: Var = Var(succ)

  val zeroVariable: Lambda = Var(zero)
  val falseVariable: Lambda = Var("false")
  val trueVariable: Lambda = Var("true")
  val idiotVariable: Lambda = Var("id")

  def addApply(l: Lambda): Lambda = {
    val applyBody = multi(List("a", "b"), NonEmptyList.of(Var("a"), Var("b")))
    l |> appAppAbs(applyBody, applyBody, apply)
  }

  def appAppAbs(l1: Lambda, l2: Lambda, name: String)(l3: Lambda): Lambda =
    App(l1, App(l2, Abs(name, l3)))

  def wrapAppThrush(l1: Lambda, name: String)(l2: Lambda): Lambda =
    appAppAbs(thrushVariable, l1, name)(l2)

  def reThrush(oldName: String, newName: String)(l: Lambda): Lambda =
    wrapAppThrush(Var(oldName), newName)(l)

  def wrapAppVireoApp(l1: Lambda, l2: Lambda): Lambda =
    App(App(vireoVariable, l1), l2)

  def appSuccVariable(l: Lambda): Lambda = App(succVariable, l)
}
