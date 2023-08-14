

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

def compile(l: Lambda): Combinator = l match {
  case Var(x) => I
  case Abs(x, Var(y)) if x == y => I
  case Abs(x, Var(y)) => K
  case Abs(x, App(y, z)) if !freeVariables(z).contains(x) => Apply(B, compile(y))
  case Abs(x, App(App(y, z1), z2)) if !freeVariables(z2).contains(x) => Apply(C, compile(y))
  case Abs(_, App(f, x)) => Apply(Apply(S, compile(Abs(x, f))), compile(x))
  case _ => throw new Exception("Unsupported lambda term")
}

def interpret(comb: Combinator): Combinator = comb match {
  case I => I
  case K => K
  case S => S
  case Apply(I, x) => x
  case Apply(K, x) => K
  case Apply(Apply(K, x), y) => x
  case Apply(Apply(Apply(S, x), y), z) =>
    val first = Apply(x, z)
    val second = Apply(y, z)
    Apply(first, second)
  case Apply(f, x) =>
    val reducedF = interpret(f)
    val reducedX = interpret(x)
    interpret(Apply(reducedF, reducedX))
  case _ => throw new Exception("Unsupported combinator application")
}
