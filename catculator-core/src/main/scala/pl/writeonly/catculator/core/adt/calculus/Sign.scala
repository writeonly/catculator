package pl.writeonly.catculator.core.adt.calculus

object Sign {

  def generate(s: Sign): String = s match {
    case Plus  => "+"
    case Minus => "-"
  }

  case object Plus extends Sign
  case object Minus extends Sign
}

trait Sign
