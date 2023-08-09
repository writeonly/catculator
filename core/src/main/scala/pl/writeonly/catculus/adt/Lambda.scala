package pl.writeonly.catculus.adt

sealed trait Lambda

case class Var(name: String) extends Lambda
case class Abs(param: String, body: Lambda) extends Lambda
