package pl.writeonly.catculus.adt.calculus

object Sign {

  def generate(s: Sign): String = s match {
    case Plus => "+"
    case Minus => "-"
  }

  final case object Plus  extends Sign
  final case object Minus extends Sign
}

trait Sign