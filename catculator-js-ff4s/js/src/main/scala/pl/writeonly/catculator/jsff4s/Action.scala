package pl.writeonly.catculator.jsff4s

sealed trait Action
//FIXME move to Action object
case class Inc(amount: Int) extends Action
case object Reset extends Action