package pl.writeonly.catculus.tagless

//import cats.tagless.{finalAlg, autoFunctorK}
//
//// Algebra dla kombinatorów Lazy K
//@finalAlg
//trait LazyKAlg[F[_]] {
//  def k: F[Any => Any => Any]
//  def s: F[(Any => Any => Any) => (Any => Any) => Any => Any]
//  def app[A, B](func: F[A => B], arg: F[A]): F[B]
//  def lit[A](a: A): F[A]
//}
//
//// Interpreter dla Lazy K w monadzie Id
//object LazyKInterpreter extends LazyKAlg[Id] {
//  def k = (x: Any) => (y: Any) => x
//  def s = (x: Any => Any => Any) => (y: Any => Any) => (z: Any) => x(z)(y(z))
//  def app[A, B](func: Id[A => B], arg: Id[A]): Id[B] = func(arg)
//  def lit[A](a: A): Id[A] = a
//}
//
//type Id[A] = A
//
//// Testowanie
//import LazyKInterpreter._
//
//val test = app(app(s, k), k)
//println(test("Hello ")("World"))  // powinno wydrukować "Hello World"
