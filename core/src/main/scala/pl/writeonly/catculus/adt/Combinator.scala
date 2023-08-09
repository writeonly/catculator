package pl.writeonly.catculus.adt

sealed trait Combinator

case object S extends Combinator
case object K extends Combinator
case object I extends Combinator
