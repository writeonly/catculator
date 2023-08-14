

  def interpret(lambda: Lambda): Lambda = lambda match {
    case App(Abs(param, body), arg) => substitute(body, param, arg)
    case App(f, x) => App(interpret(f), x)
    case Abs(param, body) => Abs(param, interpret(body))
    case _ => lambda
  }

  def substitute(lambda: Lambda, param: String, value: Lambda): Lambda = lambda match {
    case Var(name) if name == param => value
    case Var(name) => Var(name)
    case Abs(p, body) if p != param => Abs(p, substitute(body, param, value))
    case Abs(p, body) => Abs(p, body)
    case App(f, x) => App(substitute(f, param, value), substitute(x, param, value))
    case Apps(fs) => Apps(fs.map(substitute(_, param, value)))
  }

