

sealed trait Combinator
case object S extends Combinator
case object K extends Combinator
case class Apply(f: Combinator, x: Combinator) extends Combinator

def compile(l: Lambda): Combinator = l match {
  case Var(x) => Apply(Apply(S, K), K) // This is the identity combinator I = S K K
  case Abs(x, Var(y)) if x == y => Apply(Apply(S, K), K)
  case Abs(x, Var(y)) => K
  case Abs(x, App(y, z)) => Apply(S, Apply(compile(Abs(x, y)), compile(Abs(x, z))))
  case _ => throw new Exception("Unsupported lambda term")
}
