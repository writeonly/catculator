package pl.writeonly.catculator.core.calculators.lazyk

import pl.writeonly.catculator.core.adt.calculus.Combinator
import pl.writeonly.catculator.core.adt.calculus.Combinator.*
import pl.writeonly.catculator.core.adt.tree.BinaryTree.*
import pl.writeonly.catculator.core.calculators.lazyk.ADT.*

object Constants {
  val sCom: ADTBT = com(S)
  val trueCom: ADTBT = com(K)
  val iCom: ADTBT = com(I)
  val falseCom: ADTBT = Node(trueCom, iCom)
  
  val appKS: ADTBT  = Node(trueCom, sCom)
  
  val bCom: ADTBT  = app3(sCom, appKS ,trueCom)
  
  def app3SI(a: ADTBT) = app3(sCom, iCom, a)
  
  def appK(a: ADTBT) = Node(trueCom, a)
  
  def app4(c1: ADTBT, c2: ADTBT, c3: ADTBT, c4: ADTBT): ADTBT =
    Node(app3(c1, c2, c3), c4)

  def app3(c1: ADTBT, c2: ADTBT, c3: ADTBT): ADTBT =
    Node(Node(c1, c2), c3)
  
  def com(c: Combinator): ADTBT  = Leaf(ADT.Com(c))

}
